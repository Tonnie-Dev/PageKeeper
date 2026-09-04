package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import android.net.Uri
import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface BookmarksUiEvent : UiEvent {

    data class OpenBook(val bookId: String) : BookmarksUiEvent

    data object ImportBook : BookmarksUiEvent

    data class FileSelected(val uri: Uri, val fileName: String) : BookmarksUiEvent

    data class ContextMenuClicked(val bookId: String) : BookmarksUiEvent

    data object DismissContextMenu : BookmarksUiEvent

    data class ViewBookmarks(val bookId: String) : BookmarksUiEvent
    data object ViewLibrary: BookmarksUiEvent

    data class DeleteBookmarks(
        val bookId: String,
        val dialogTitle: String,
        val dialogMessage: String,
        val positiveButtonText: String,
        val negativeButtonText: String
    ) : BookmarksUiEvent

    data object CancelDeleteDialog : BookmarksUiEvent

    data object ConfirmDelete : BookmarksUiEvent

    data object SearchClicked : BookmarksUiEvent

    data object ExitSearch : BookmarksUiEvent

    data object ClearSearchClicked : BookmarksUiEvent
}
