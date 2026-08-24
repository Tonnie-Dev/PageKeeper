package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.GlobalBookmarkUiItem

data class BookmarksUiState(
    val globalBookmarkUiItems: List<GlobalBookmarkUiItem> = emptyList(),
    val selectedMenuItemId: String? = null,
    val deleteDialogState: DeleteDialogState = DeleteDialogState(),
) : UiState {

    data class DeleteDialogState(
        val showDeleteDialog: Boolean = false,
        val bookId: String? = null,
    )
}
