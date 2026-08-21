package com.tonyxlab.pagekeeper.presentation.screens.reader

import androidx.lifecycle.viewModelScope
import com.tonyxlab.pagekeeper.data.local.datastore.FontDataStore
import com.tonyxlab.pagekeeper.data.parser.Fb2Parser
import com.tonyxlab.pagekeeper.data.parser.mapper.toReaderBook
import com.tonyxlab.pagekeeper.domain.model.Bookmark
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.domain.repository.BookmarkRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkHandler
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.toBookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.handling.ChapterHandler
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.mapper.toChapterSections
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.handling.ControlsHandler
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.handling.FontHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

typealias ReadBaseViewModel = BaseViewModel<ReaderUiState, ReaderUiEvent, ReaderActionEvent>

class ReadViewModel(
    private val fb2Parser: Fb2Parser,
    private val bookRepository: BookRepository,
    private val bookmarkRepository: BookmarkRepository,
    fontDataStore: FontDataStore,
    bookId: String,
) : ReadBaseViewModel(initialState = ReaderUiState()) {

    private var autoHideJob: Job? = null
    private var progressSaveJob: Job? = null

    private val chapterHandler = ChapterHandler(
            updateState = ::updateState,
            sendActionEvent = ::sendActionEvent
    )

    private val controlsHandler = ControlsHandler(
            currentState = { currentState },
            updateState = ::updateState,
            restartControlsAutoHideTimer = ::restartControlsAutoHideTimer,
            cancelControlsAutoHideTimer = ::cancelControlsAutoHideTimer
    )

    private val fontHandler = FontHandler(
            fontDataStore = fontDataStore,
            coroutineScope = viewModelScope,
            currentState = { currentState },
            updateState = ::updateState,
            restartControlsAutoHideTimer = ::restartControlsAutoHideTimer,
            onSaveError = {
                sendActionEvent(ReaderActionEvent.ShowToast("Unable to save font size."))
            }
    )

    private val bookmarkHandler = BookmarkHandler(
            updateState = ::updateState,
            sendActionEvent = ::sendActionEvent,
            currentState = { currentState },
            bookmarkRepository = bookmarkRepository,
            coroutineScope = viewModelScope,
            onSaveError = {
                sendActionEvent(ReaderActionEvent.ShowToast("Unable to save bookmark."))
            },
            onDeleteError = {
                sendActionEvent(ReaderActionEvent.ShowToast("Unable to delete bookmark."))
            }
    )

    init {
        fontHandler.loadFontSize()
        loadBook(bookId)
        observeBookmarks(bookId)
    }

    override fun onEvent(event: ReaderUiEvent) {
        when (event) {
            // Read UiEvents
            ReaderUiEvent.ToggleAutoRotate -> controlsHandler.onToggleAutoRotate()
            ReaderUiEvent.IncreaseFontSize -> fontHandler.onIncreaseFontSize()
            ReaderUiEvent.DecreaseFontSize -> fontHandler.onDecreaseFontSize()
            ReaderUiEvent.FontSizeClicked -> fontHandler.showFontSizePanel()
            ReaderUiEvent.ExitReader -> exitReader()
            ReaderUiEvent.ReadingAreaClicked -> controlsHandler.onReadingAreaClicked()
            ReaderUiEvent.ToggleFavorite -> toggleFavorite()
            is ReaderUiEvent.PreviewFontSizeChange -> fontHandler.previewFontSize(event.fontSize)
            is ReaderUiEvent.FontSizeChangeFinished -> fontHandler.finishAndSaveFontSizeChange()
            is ReaderUiEvent.ReadingPositionChanged ->
                onReadingPositionChanged(
                        blockIndex = event.blockIndex,
                        textOffset = event.textOffset
                )

            ReaderUiEvent.ViewChapters -> chapterHandler.viewChapters()
            ReaderUiEvent.ChaptersJumpConsumed -> chapterHandler.onConsumeChapterJump()

            // Chapter UiEvents
            ReaderUiEvent.BackClicked -> chapterHandler.exitChapters()
            is ReaderUiEvent.ChapterSelected -> chapterHandler.selectChapter(event.startBlockIndex)
            is ReaderUiEvent.SectionClicked -> {

            }

            ReaderUiEvent.ViewBookmarks -> viewBookmarks()

            // Bookmark UiEvents
            ReaderUiEvent.AddBookmark -> bookmarkHandler.onAddBookmark()
            is ReaderUiEvent.ColorSelected -> bookmarkHandler.onSelectColor(event.color)
            ReaderUiEvent.DismissBookmarkDialog -> bookmarkHandler.onDismissBookmarkDialog()
            ReaderUiEvent.NavigateBack -> bookmarkHandler.onExitBookmark()
            ReaderUiEvent.SaveBookmark -> bookmarkHandler.onSaveBookmark()
            is ReaderUiEvent.SelectBookmark -> {}
            is ReaderUiEvent.ShowPopupMenu -> bookmarkHandler.onShowPopupMenu(event.bookmarkUiItem)
            is ReaderUiEvent.EditBookmark -> bookmarkHandler.onEditBookmark(event.bookmarkUiItem)
            is ReaderUiEvent.DeleteBookmarkClicked -> bookmarkHandler.onClickDelete()
            ReaderUiEvent.DismissPopupMenu -> bookmarkHandler.onDismissPopupMenu()
            ReaderUiEvent.ConfirmDeleteBookmark -> bookmarkHandler.onConfirmDelete()
            ReaderUiEvent.CancelDeleteBookmark -> bookmarkHandler.onCancelDeleteDialog()
        }
    }

    fun loadBook(bookId: String) {
        if (currentState.book?.id == bookId && (currentState.document != null || currentState.isLoading)) {
            return
        }

        launchCatching(
                context = Dispatchers.IO,
                onStart = {
                    updateState {
                        it.copy(
                                book = null,
                                document = null,
                                isLoading = true
                        )
                    }
                },
                onError = ::onBookLoadFailed,
                onCompletion = { updateState { it.copy(isLoading = false) } }
        ) {
            bookRepository.markAsOpened(bookId = bookId)
            val book = bookRepository.getBookById(bookId)

            updateState { it.copy(book = book) }

            fb2Parser.parse(File(book.filePath))
                    .fold(
                            onSuccess = { parsedBook ->

                                val document = parsedBook.toReaderBook()

                                val currentBlockIndex = book.lastReadBlockIndex.coerceIn(
                                        minimumValue = 0,
                                        maximumValue = document.blocks.lastIndex.coerceAtLeast(0)
                                )

                                updateState { state ->
                                    state.copy(
                                            document = document,
                                            book = state.book?.copy(
                                                    totalBlockCount = document.blocks.size
                                            ),
                                            chapterSections = document.toChapterSections(),
                                            readingPosition = state.readingPosition.copy(
                                                    currentBlockIndex = currentBlockIndex
                                            )
                                    )
                                }
                            },
                            onFailure = ::onBookLoadFailed
                    )
        }
    }

    private fun onBookLoadFailed(error: Throwable) {
        updateState { it.copy(document = null) }
        sendActionEvent(
                ReaderActionEvent.ShowToast(
                        error.message ?: "Unable to read this book."
                )
        )
    }

    private fun observeBookmarks(bookId: String) {

        launchCatching {

            bookmarkRepository.observeBookmarks(bookId = bookId)
                    .collect { bookmarks ->

                        updateState { state ->

                            state.copy(
                                    bookmarkUiState = state.bookmarkUiState.copy(
                                            bookmarkUiItems = bookmarks.map(Bookmark::toBookmarkUiItem)
                                    )
                            )
                        }
                    }
        }
    }

    private fun toggleFavorite() {
        val book = currentState.book ?: return
        val updatedFavorite = !book.isFavorite

        updateState { state ->
            state.copy(book = book.copy(isFavorite = updatedFavorite))
        }

        launchCatching(
                context = Dispatchers.IO,
                onError = {
                    updateState { state ->
                        state.copy(book = state.book?.copy(isFavorite = book.isFavorite))
                    }
                    sendActionEvent(ReaderActionEvent.ShowToast("Unable to update favorite status."))
                }
        ) {
            bookRepository.updateFavorite(book.id, updatedFavorite)
        }
    }

    private fun cancelControlsAutoHideTimer() {
        autoHideJob?.cancel()
        autoHideJob = null
    }

    private fun restartControlsAutoHideTimer() {
        cancelControlsAutoHideTimer()
        if (currentState.controlMode == ReadingControlMode.Immersive) {
            return
        }

        autoHideJob = launch {

            delay(AUTOHIDE_TIMEOUT.milliseconds)

            updateState { state -> state.copy(controlMode = ReadingControlMode.Immersive) }
        }
    }

    private fun onReadingPositionChanged(
        blockIndex: Int,
        textOffset: Int
    ) {
        val document = currentState.document ?: return

        val totalBlockCount = document.blocks.size

        updateState { state ->
            state.copy(
                    book = state.book?.copy(
                            lastReadBlockIndex = blockIndex,
                            totalBlockCount = totalBlockCount
                    ),
                    readingPosition = state.readingPosition.copy(
                            currentBlockIndex = blockIndex,
                            textOffset = textOffset
                    )
            )
        }

        progressSaveJob?.cancel()

        progressSaveJob = launch {
            delay(1000.milliseconds)
            saveReadingPosition(
                    blockIndex = blockIndex,
                    totalBlockCount = totalBlockCount
            )
        }
    }

    private fun saveReadingPosition(
        blockIndex: Int,
        totalBlockCount: Int
    ) {

        val bookId = currentState.book?.id ?: return
        launchCatching(context = Dispatchers.IO) {

            bookRepository.updateReadingProgress(
                    bookId = bookId,
                    lastReadBlockIndex = blockIndex,
                    totalBlockCount = totalBlockCount
            )
        }
    }

    private fun exitReader() {
        sendActionEvent(ReaderActionEvent.ExitReader)
    }

    private fun viewBookmarks() {
        sendActionEvent(ReaderActionEvent.NavigateToBookmarksView)
    }

    private companion object {
        const val AUTOHIDE_TIMEOUT = 5_000L
    }
}
