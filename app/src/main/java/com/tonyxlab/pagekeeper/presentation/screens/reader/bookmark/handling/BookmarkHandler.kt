package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState

class BookmarkHandler(
    private val updateState: ((ReaderUiState) -> ReaderUiState) -> Unit,
    private val sendActionEvent: (ReaderActionEvent) -> Unit
) {

    fun onAddBookmark() {
        updateState { state ->
            state.copy(
                    bookmarkUiState = state.bookmarkUiState.copy(

                            dialogInputState = state.bookmarkUiState.dialogInputState.copy(
                                    showBookmarkDialog = true
                            )
                    )
            )
        }
    }

    fun onSaveBookmark() {
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

    fun onExitBookmark() {
        sendActionEvent(ReaderActionEvent.ExitBookmark)
    }
}