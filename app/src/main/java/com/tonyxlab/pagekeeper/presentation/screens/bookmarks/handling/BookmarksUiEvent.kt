package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface BookmarksUiEvent : UiEvent {

    data class BookClicked(val bookId: String) : BookmarksUiEvent

    data class ContextMenuClicked(val bookId: String) : BookmarksUiEvent

    data object DismissContextMenu : BookmarksUiEvent

    data class ViewAllBookmarks(val bookId: String) : BookmarksUiEvent

    data class DeleteAllBookmarks(val bookId: String) : BookmarksUiEvent

    data object CancelDeleteDialog : BookmarksUiEvent

    data object ConfirmDelete : BookmarksUiEvent
}
