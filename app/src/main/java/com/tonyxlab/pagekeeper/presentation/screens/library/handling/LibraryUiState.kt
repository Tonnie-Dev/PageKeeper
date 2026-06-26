package com.tonyxlab.pagekeeper.presentation.screens.library.handling

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState

data class LibraryUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = true,
    val isImporting: Boolean = false,
    val dialog: LibraryDialog? = null,
    val selectedDrawerDestination: LibraryDrawerDestination = LibraryDrawerDestination.Library,
    val searchState: SearchState = SearchState(),
    val selectionState: SelectionState = SelectionState()
) : UiState {

    @Stable
    data class SearchState(
        val searchTextFieldState: TextFieldState = TextFieldState(),
        val isSearchMode: Boolean = false,
        val searchResults: List<Book> = emptyList(),
    )

    @Stable
    data class SelectionState(
        val isSelectionMode: Boolean = false,
        val selectedBooksIds: Set<String> = emptySet()
    ) {

        val selectedCount: Int
            get() = selectedBooksIds.size
    }
}

data class LibraryDialog(
    val title: String,
    val message: String,
    val positiveButtonText: String,
    val negativeButtonText: String? = null,
    val type: LibraryDialogType,
    val bookId: String? = null,
)

enum class LibraryDialogType {
    DeleteBook,
    DeleteSelectedBooks,
    UnsupportedFile,
}

enum class LibraryDrawerDestination {
    Library,
    Favorites,
    Finished,
}

