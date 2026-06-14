package com.tonyxlab.pagekeeper.presentation.screens.library.handling

import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState

data class LibraryUiState(
    val books: List<Book> = emptyList(),
    val isLoading: Boolean = false,
    val isImporting: Boolean = false,
    val dialog: LibraryDialog? = null
): UiState

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
    UnsupportedFile,
}
