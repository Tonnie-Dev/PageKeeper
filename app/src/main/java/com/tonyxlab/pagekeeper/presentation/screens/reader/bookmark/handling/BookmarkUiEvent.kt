package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface BookmarkUiEvent : UiEvent{

    object AddBookmark: BookmarkUiEvent
    object SaveBookmark: BookmarkUiEvent
    object DismissBookmarkDialog: BookmarkUiEvent
}
