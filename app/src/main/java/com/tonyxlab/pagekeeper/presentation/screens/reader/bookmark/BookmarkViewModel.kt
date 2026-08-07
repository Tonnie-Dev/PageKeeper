package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark

import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiState

typealias BookmarkBaseViewModel =
        BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkActionEvent>

class BookmarkViewModel : BookmarkBaseViewModel(initialState = BookmarkUiState()) {

    override fun onEvent(event: BookmarkUiEvent) {

        when (event) {
            BookmarkUiEvent.AddBookmark -> onAddBookmark()
            BookmarkUiEvent.SaveBookmark -> onSaveBookmark()
            BookmarkUiEvent.DismissBookmarkDialog -> onDismissBookmarkDialog()
            is BookmarkUiEvent.ColorSelected -> onSelectColor(event.color)
            BookmarkUiEvent.NavigateBack -> onExitBookmark()
        }
    }

    private fun onAddBookmark() {
        updateState { state ->
            state.copy(
                    dialogInputState = state.dialogInputState.copy(
                            showBookmarkDialog = true
                    )
            )
        }
    }

    private fun onSaveBookmark() {
        updateState { state ->
            state.copy(
                    dialogInputState = state.dialogInputState.copy(
                            showBookmarkDialog = false
                    )
            )
        }
    }

    private fun onSelectColor(color: BookmarkColor) {
        updateState { state ->
            state.copy(
                    dialogInputState = state.dialogInputState.copy(
                            selectedColor = color
                    )
            )
        }
    }

    private fun onDismissBookmarkDialog() {
        updateState { state ->
            state.copy(
                    dialogInputState = state.dialogInputState.copy(
                            showBookmarkDialog = false
                    )
            )
        }
    }

    private fun onExitBookmark() {
       sendActionEvent(BookmarkActionEvent.NavigateBackToRead)
    }
}

