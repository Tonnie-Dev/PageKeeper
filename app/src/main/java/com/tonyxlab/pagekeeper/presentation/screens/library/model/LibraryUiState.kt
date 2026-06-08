package com.tonyxlab.pagekeeper.presentation.screens.library.model

import com.tonyxlab.pagekeeper.domain.model.Book

data class LibraryUiState(
    val books: List<Book> = emptyList(),
    val isImporting: Boolean = false,
    val showUnsupportedDialog: Boolean = false,
    val showDeleteDialogFor: Book? = null,
    val message: String? = null
)