package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiState

typealias BookmarkBaseViewModel =
    BaseViewModel<BookmarkUiState, BookmarkUiEvent, BookmarkActionEvent>

class BookmarkViewModel : BookmarkBaseViewModel(initialState = BookmarkUiState()) {

    override fun onEvent(event: BookmarkUiEvent) = Unit
}
