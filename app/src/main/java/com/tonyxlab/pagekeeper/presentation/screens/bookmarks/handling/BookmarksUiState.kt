package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.GlobalBookmarkUiItem

data class BookmarksUiState(
    val globalBookmarkUiItems: List<GlobalBookmarkUiItem> = emptyList(),
    val selectedMenuItemId: String? = null,
    val isImporting: Boolean = false,
    val bookmarkDialogState: BookmarksDialogState? = null,
    val searchState: SearchState = SearchState(),
) : UiState {

    @Stable
    data class SearchState(
        val searchTextFieldState: TextFieldState = TextFieldState(),
        val isSearchMode: Boolean = false,
        val searchResults: List<GlobalBookmarkUiItem> = emptyList(),
    )
}

data class BookmarksDialogState(
    val title: String,
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String? = null,
    val type: BookmarksDialogType,
    val bookId: String? = null,
)

enum class BookmarksDialogType {
    DeleteBookmarks,
    UnsupportedFile
}
