package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState

class BookmarksViewModel : BaseViewModel<BookmarksUiState, BookmarksUiEvent, BookmarksActionEvent>(
    initialState = BookmarksUiState(),
) {
    override fun onEvent(event: BookmarksUiEvent) = Unit
}
