package com.tonyxlab.pagekeeper.presentation.screens.library

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState

class LibraryViewModel : BaseViewModel<LibraryUiState, LibraryUiEvent, LibraryActionEvent>() {
    override val initialState: LibraryUiState = LibraryUiState

    override fun onEvent(event: LibraryUiEvent) {
        // TODO: Handle events
    }
}
