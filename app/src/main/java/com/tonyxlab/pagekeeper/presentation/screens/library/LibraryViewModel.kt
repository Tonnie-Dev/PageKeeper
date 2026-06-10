package com.tonyxlab.pagekeeper.presentation.screens.library

import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState

class LibraryViewModel(
    private val bookRepository: BookRepository
) : BaseViewModel<LibraryUiState, LibraryUiEvent, LibraryActionEvent>() {
    override val initialState: LibraryUiState = LibraryUiState()

    override fun onEvent(event: LibraryUiEvent) {
        when(event) {
            is LibraryUiEvent.BookSelected -> TODO()
            is LibraryUiEvent.BookmarkBookClicked -> TODO()
            is LibraryUiEvent.ConfirmDeleteBook -> TODO()
            is LibraryUiEvent.DeleteBookClicked -> TODO()
            LibraryUiEvent.DismissDialog -> TODO()
            is LibraryUiEvent.FavoriteBookClicked -> TODO()
            is LibraryUiEvent.FileSelected -> TODO()
            LibraryUiEvent.ImportBookClicked -> TODO()
            is LibraryUiEvent.ShareBookClicked -> TODO()
        }
    }
}
