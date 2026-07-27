package com.tonyxlab.pagekeeper.presentation.screens.read

import com.tonyxlab.pagekeeper.data.local.datastore.FontDataStore
import com.tonyxlab.pagekeeper.data.parser.Fb2Parser
import com.tonyxlab.pagekeeper.domain.model.toReaderBook
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReaderFontSize
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingControlMode
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.coercedFontSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

typealias ReadBaseViewModel = BaseViewModel<ReadUiState, ReadUiEvent, ReadActionEvent>

class ReadViewModel(
    private val fb2Parser: Fb2Parser,
    private val bookRepository: BookRepository,
    private val fontDataStore: FontDataStore,
    bookId: String,
) : ReadBaseViewModel(initialState = ReadUiState()) {

    private var autoHideJob: Job? = null
    private var progressSaveJob: Job? = null

    init {
        loadFontSize()
        loadBook(bookId)

    }

    override fun onEvent(event: ReadUiEvent) {
        when (event) {
            ReadUiEvent.ToggleAutoRotate -> onToggleAutoRotate()
            ReadUiEvent.IncreaseFontSize -> onIncreaseFontSize()
            ReadUiEvent.DecreaseFontSize -> onDecreaseFontSize()
            ReadUiEvent.FontSizeClicked -> showFontSizePanel()
            ReadUiEvent.ExitReader -> exitReader()
            ReadUiEvent.ReadingAreaClicked -> onReadingAreaClicked()
            ReadUiEvent.ToggleFavorite -> toggleFavorite()
            is ReadUiEvent.PreviewFontSizeChange -> previewFontSize(event.fontSize)
            is ReadUiEvent.FontSizeChangeFinished -> finishAndSaveFontSizeChange()
            is ReadUiEvent.ReadingPositionChanged ->
                onReadingPositionChanged(blockIndex = event.blockIndex)

            ReadUiEvent.ViewChapters -> {}
        }
    }

    private fun loadFontSize() {

        launch {

            val savedFontSize = fontDataStore.fontSize.first()
            updateState { state ->
                state.copy(
                        fontSizeState = state.fontSizeState.copy(
                                fontSize = savedFontSize,
                                previewFontSize = savedFontSize
                        )
                )
            }
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
                                updateState { state ->
                                    state.copy(
                                            document = document,
                                            book = state.book?.copy(
                                                    totalBlockCount = document.blocks.size
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
                ReadActionEvent.ShowToast(
                        error.message ?: "Unable to read this book."
                )
        )
    }

    private fun onToggleAutoRotate() {
        updateState { state ->
            state.copy(
                    orientation = when (state.orientation) {
                        ReadingOrientation.AUTO_ROTATE -> ReadingOrientation.LANDSCAPE_LOCK
                        ReadingOrientation.LANDSCAPE_LOCK -> ReadingOrientation.AUTO_ROTATE
                    }
            )
        }
    }

    private fun showFontSizePanel() {
        updateState { state ->
            state.copy(
                    controlMode = ReadingControlMode.FontSizePanel,
                    fontSizeState = state.fontSizeState.copy(
                            previewFontSize = state.fontSizeState.fontSize
                    )
            )
        }
        restartControlsAutoHideTimer()

    }

    private fun previewFontSize(fontSize: Float) {
        updateState { state ->
            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            previewFontSize = fontSize.coercedFontSize
                    )
            )
        }

        restartControlsAutoHideTimer()
    }

    private fun finishAndSaveFontSizeChange() {
        val updatedFontSize =
            currentState.fontSizeState.previewFontSize.coercedFontSize

        updateState { state ->
            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            fontSize = updatedFontSize,
                            previewFontSize = updatedFontSize
                    )
            )
        }

        saveFontSize()
    }

    private fun onIncreaseFontSize() {
        updateState { state ->
            val updatedFontSize =
                (state.fontSizeState.fontSize + FONT_SIZE_STEP)
                        .coerceInFontRange()

            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            fontSize = updatedFontSize,
                            previewFontSize = updatedFontSize
                    )
            )
        }
        saveFontSize()
        restartControlsAutoHideTimer()
    }

    private fun onDecreaseFontSize() {
        updateState { state ->
            val updatedFontSize =
                (state.fontSizeState.fontSize - FONT_SIZE_STEP)
                        .coerceInFontRange()

            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            fontSize = updatedFontSize,
                            previewFontSize = updatedFontSize
                    )
            )
        }
        saveFontSize()
        restartControlsAutoHideTimer()
    }

    private fun saveFontSize() {
        val fontSize = currentState.fontSizeState.fontSize

        launchCatching(
                context = Dispatchers.IO,
                onError = {
                    sendActionEvent(ReadActionEvent.ShowToast("Unable to save font size."))
                }
        ) {
            fontDataStore.saveFontSize(fontSize)
        }
    }

    private fun Float.coerceInFontRange(): Float {
        return coerceIn(ReaderFontSize.MIN, ReaderFontSize.MAX)
                .toInt()
                .toFloat()
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
                    sendActionEvent(ReadActionEvent.ShowToast("Unable to update favorite status."))
                }
        ) {
            bookRepository.updateFavorite(book.id, updatedFavorite)
        }
    }

    private fun onReadingAreaClicked() {
        when (currentState.controlMode) {
            ReadingControlMode.Immersive -> {
                showScreenControls()
            }

            else -> {
                hideScreenControls()
            }
        }
    }

    private fun showScreenControls() {

        updateState { state -> state.copy(controlMode = ReadingControlMode.DefaultToolbar) }
        restartControlsAutoHideTimer()
    }

    private fun hideScreenControls() {
        autoHideJob?.cancel()
        autoHideJob = null
        updateState { state -> state.copy(controlMode = ReadingControlMode.Immersive) }
    }

    private fun restartControlsAutoHideTimer() {
        autoHideJob?.cancel()
        if (currentState.controlMode == ReadingControlMode.Immersive) {
            autoHideJob = null
            return
        }

        autoHideJob = launch {

            delay(AUTOHIDE_TIMEOUT.milliseconds)

            updateState { state -> state.copy(controlMode = ReadingControlMode.Immersive) }
        }
    }

    private fun onReadingPositionChanged(blockIndex: Int) {
        val document = currentState.document ?: return

        val totalBlockCount = document.blocks.size

        updateState { state ->
            state.copy(
                    book = state.book?.copy(
                            lastReadBlockIndex = blockIndex,
                            totalBlockCount = totalBlockCount
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
        sendActionEvent(ReadActionEvent.ExitReader)
    }

    private companion object {
        const val FONT_SIZE_STEP = 1f
        const val AUTOHIDE_TIMEOUT = 5_000L
    }
}
