package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling

import androidx.annotation.StringRes
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface BookmarksActionEvent : ActionEvent {

    data object OpenFilePicker : BookmarksActionEvent
    data object NavigateToLibrary : BookmarksActionEvent

    data class NavigateToBookmarkPage(val bookId: String) : BookmarksActionEvent
    data class ShowSnackbar(
        @StringRes
        val messageRes: Int,
        @StringRes
        val actionLabelRes: Int = R.string.blank_text,
        val event: BookmarksUiEvent? = null,
        val isError: Boolean = false
    ) : BookmarksActionEvent
}
