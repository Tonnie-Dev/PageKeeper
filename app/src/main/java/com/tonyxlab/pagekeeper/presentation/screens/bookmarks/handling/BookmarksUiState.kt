package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.GlobalBookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialogType
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState.SearchState

data class BookmarksUiState(
    val globalBookmarkUiItems: List<GlobalBookmarkUiItem> = emptyList(),
    val selectedMenuItemId: String? = null,
    val isImporting: Boolean = false,
    val bookmarkDialogState : BookmarksDialogState? = null,
   // val deleteDialogState: DeleteDialogState = DeleteDialogState(),
    val searchState: BookmarksUiState.SearchState = SearchState(),
) : UiState {

  /*  data class DeleteDialogState(
        val showDeleteDialog: Boolean = false,
        val bookId: String? = null,
    )*/

    @Stable
    data class SearchState(
        val searchTextFieldState: TextFieldState = TextFieldState(),
        val isSearchMode: Boolean = false,
        val searchResults: List<Book> = emptyList(),
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
