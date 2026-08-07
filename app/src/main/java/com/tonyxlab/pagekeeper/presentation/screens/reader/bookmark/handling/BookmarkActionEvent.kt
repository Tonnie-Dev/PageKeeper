package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface BookmarkActionEvent : ActionEvent{
    object NavigateBackToRead:BookmarkActionEvent
}
