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
    private val onSaveError: () -> Unit,
    private val onDeleteError: () -> Unit
) {

    fun onAddBookmark() {

        val currentState = currentState()

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

        updateTextField(text = bookmarkText.toString())

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(

                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showEditBookmarkDialog = true,
                                    blockIndex = blockIndex,
                                    chapterTitle = chapterTitle,
                                    editingBookmarkId = null
                            )
                    )
            )
        }
    }

    fun onEditBookmark(bookmarkUiItem: BookmarkUiItem) {

        updateTextField(bookmarkUiItem.text)

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            selectedBookmarkId = null,
                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showEditBookmarkDialog = true,
                                    selectedColor = bookmarkUiItem.color,
                                    blockIndex = bookmarkUiItem.blockIndex,
                                    editingBookmarkId = bookmarkUiItem.id
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

        coroutineScope.launch(Dispatchers.IO) {
            try {

                val editingBookmarkId = dialogState.editingBookmarkId

                if (editingBookmarkId == null) {

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
                    bookmarkRepository.insertBookmark(bookMarkToSave)

                } else {
                    val existingBookmark = bookmarkRepository
                            .getBookmarkById(bookmarkId = editingBookmarkId)

                    val updatedBookmark = existingBookmark.copy(
                            text = bookmarkText,
                            color = dialogState.selectedColor
                    )
                    bookmarkRepository.updateBookmark(updatedBookmark)
                }
                closeBookmarkEditDialog()
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

    fun onClickDelete() {

        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showDeleteBookmarkDialog = true
                            )
                    )
            )
        }
    }

    fun onCancelDeleteDialog() {
        closeBookmarkDeleteDialog()
    }

    fun onConfirmDelete() {

        coroutineScope.launch(Dispatchers.IO) {

            try {

                val selectedBookmark =
                    currentState().bookmarkUiState.selectedBookmarkId
                        ?: return@launch
                bookmarkRepository.deleteBookmarkById(bookmarkId = selectedBookmark)

                closeBookmarkDeleteDialog()

            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                onCancelDeleteDialog()
            }
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

    fun onDismissBookmarkDialog() {
        closeBookmarkEditDialog()
    }
    fun onExitBookmark() {
        sendActionEvent(ReaderActionEvent.ExitBookmark)
    }

    private fun updateTextField(text: String) {

        currentState().bookmarkUiState.dialogInputState.textFieldState.edit {
            replace(
                    start = 0,
                    end = length,
                    text = text
            )
        }
    }

    private fun closeBookmarkEditDialog() {
        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showEditBookmarkDialog = false,
                                    editingBookmarkId = null
                            )
                    )
            )
        }
    }

    private fun closeBookmarkDeleteDialog() {
        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(
                            selectedBookmarkId = null,
                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showDeleteBookmarkDialog = false
                            )
                    )
            )
        }

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