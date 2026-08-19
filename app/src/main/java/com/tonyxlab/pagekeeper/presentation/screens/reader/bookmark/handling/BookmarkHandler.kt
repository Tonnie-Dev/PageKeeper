package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import com.tonyxlab.pagekeeper.domain.model.Bookmark
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.domain.repository.BookmarkRepository
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.BookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.util.findCurrentChapter
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookmarkHandler(
    private val bookmarkRepository: BookmarkRepository,
    private val coroutineScope: CoroutineScope,
    private val currentState: () -> ReaderUiState,
    private val updateState: ((ReaderUiState) -> ReaderUiState) -> Unit,
    private val sendActionEvent: (ReaderActionEvent) -> Unit,
    private val onSaveError: () -> Unit
) {

    fun onAddBookmark() {

        val currentState = currentState()
        val readerBook = currentState.document ?: return
        val blockIndex = currentState.readingPosition.currentBlockIndex

        val block =
            currentState.document?.blocks
                    ?.getOrNull(currentState.readingPosition.currentBlockIndex)
        val bookmarkText = when (block) {

            is ReaderContentBlock.Paragraph ->
                block.text
                        .drop(currentState.readingPosition.textOffset)
                        .take(BOOKMARK_PREVIEW_LENGTH)

            else -> ""
        }

        val chapterTitle =
            currentState.chapterSections
                    .findCurrentChapter(blockIndex)
                    ?.title
                    .orEmpty()

        currentState.bookmarkUiState.dialogInputState.textFieldState.edit {
            replace(
                    start = 0,
                    end = length,
                    text = bookmarkText
            )
        }

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(

                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showBookmarkDialog = true,
                                    blockIndex = blockIndex,
                                    chapterTitle = chapterTitle

                            )
                    )
            )
        }
    }

    fun onSaveBookmark() {
        val currentState = currentState()

        val dialogState = currentState.bookmarkUiState.dialogInputState

        val bookId = currentState.book?.id ?: return
        val blockIndex = dialogState.blockIndex ?: return

        val bookmarkText = dialogState
                .textFieldState
                .text
                .toString()
                .trim()

        bookmarkText.ifEmpty { return }

        val bookMarkToSave = Bookmark(
                id = 0,
                bookId = bookId,
                blockIndex = blockIndex,
                textOffset = currentState.readingPosition.textOffset,
                text = bookmarkText,
                chapterTitle = dialogState.chapterTitle,
                color = dialogState.selectedColor,
                createdAt = System.currentTimeMillis()
        )

        coroutineScope.launch(Dispatchers.IO) {
            try {

                bookmarkRepository.insertBookmark(bookMarkToSave)
                updateState { state ->
                    state.copy(
                            bookmarkUiState = state.bookmarkUiState.copy(
                                    dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                            showBookmarkDialog = false
                                    )
                            )
                    )
                }

            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                onSaveError()
            }
        }
    }

    fun onSelectColor(color: BookmarkColor) {
        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    selectedColor = color
                            )
                    )
            )
        }
    }

    fun onDismissBookmarkDialog() {
        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showBookmarkDialog = false
                            )
                    )
            )
        }
    }
    fun onShowPopupMenu(bookmarkUiItem: BookmarkUiItem) {

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            selectedBookmarkId = bookmarkUiItem.id,

                    )
            )
        }
    }


    fun onDismissPopupMenu() {

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            selectedBookmarkId = null
                    )
            )
        }
    }
    fun onEditBookmark() {

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            selectedBookmarkId = null
                    )
            )
        }
    }

    fun onDeleteBookmark() {

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            selectedBookmarkId = null
                    )
            )
        }
    }
    fun onExitBookmark() {
        sendActionEvent(ReaderActionEvent.ExitBookmark)
    }

    private fun ReaderContentBlock.bookmarkText(): String =
        when (this) {
            is ReaderContentBlock.ChapterTitle -> text
            is ReaderContentBlock.Paragraph -> text.text
            is ReaderContentBlock.Quote -> text.text
        }
                .replace(Regex("\\s+"), " ")
                .trim()

    companion object {

        const val BOOKMARK_PREVIEW_LENGTH = 100
    }
}