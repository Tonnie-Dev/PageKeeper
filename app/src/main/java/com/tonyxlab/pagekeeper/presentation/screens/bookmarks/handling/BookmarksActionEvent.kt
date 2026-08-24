package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface BookmarksActionEvent : ActionEvent{

    data class NavigateToBookmarkPage(val bookId: String) : BookmarksActionEvent
    data class ShowToast(val message: String) : BookmarksActionEvent
}
