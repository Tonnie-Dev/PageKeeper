package com.tonyxlab.pagekeeper.presentation.screens.library.handling

class SelectionHandler(
    private val currentState: ()-> LibraryUiState,
    private val updateState: ((LibraryUiState) -> LibraryUiState) -> Unit,
) {

    fun enterSelectionMode(bookId: String) {
        updateState {
            it.copy(
                    selectionState = it.selectionState.copy(
                            isSelectionMode = true,
                            selectedBooksIds = setOf(bookId)
                    )
            )
        }
    }

    fun toggleBookSelection(bookId: String) {
        val selectedBooksIds = currentState().selectionState.selectedBooksIds
        val updatedSelectedBooksIds = if (bookId in selectedBooksIds) {
            selectedBooksIds - bookId
        } else {
            selectedBooksIds + bookId
        }

        updateState {
            it.copy(
                    selectionState = it.selectionState.copy(
                            isSelectionMode = updatedSelectedBooksIds.isNotEmpty(),
                            selectedBooksIds = updatedSelectedBooksIds
                    )
            )
        }
    }

    fun exitSelectionMode() {
        updateState {
            it.copy(
                    selectionState = LibraryUiState.SelectionState()
            )
        }
    }

    fun selectedBookIds(): Set<String> = currentState().selectionState.selectedBooksIds
}
