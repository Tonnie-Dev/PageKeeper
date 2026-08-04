package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiState

typealias BookmarkBaseViewModel =
    BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkActionEvent>

class BookmarkViewModel : BookmarkBaseViewModel(initialState = BookmarkUiState()) {

    override fun onEvent(event: BookmarkUiEvent){

        when(event){

            BookmarkUiEvent.AddBookmark ->onAddBookmark()
            BookmarkUiEvent.SaveBookmark -> onSaveBookmark()
            BookmarkUiEvent.DismissBookmarkDialog -> onDismissBookmarkDialog()
        }
    }

    private fun onAddBookmark() {

        updateState { state -> state.copy(showBookmarkDialog = true) }
    }

    private fun onSaveBookmark() {

        updateState { state -> state.copy(showBookmarkDialog = false) }
    }

    private fun onDismissBookmarkDialog() {
        updateState { state -> state.copy(showBookmarkDialog = false) }
    }
}

