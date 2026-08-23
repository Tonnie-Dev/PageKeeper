package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import com.tonyxlab.pagekeeper.domain.repository.BookmarkRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.toGlobalBookmarkUiItem
import timber.log.Timber

typealias BookmarksBaseViewModel = BaseViewModel<BookmarksUiState, BookmarksUiEvent, BookmarksActionEvent>

class BookmarksViewModel(
    private val repository: BookmarkRepository
) : BookmarksBaseViewModel(initialState = BookmarksUiState()) {

    init {
        observeBooksWithBookmarks()
    }

    override fun onEvent(event: BookmarksUiEvent) {

    }

    private fun observeBooksWithBookmarks() {

        launch {
            repository
                    .observeBooksWithBookmarks()
                    .collect { books ->
                        Timber.tag("BookmarksViewModel").i("Received updated bookmarks - isEmpty: ${books.size}")
                        updateState { state ->
                            state.copy(
                                    globalBookmarkUiItems =
                                        books.map { it.toGlobalBookmarkUiItem() }
                            )
                        }
                    }
        }

    }
}
