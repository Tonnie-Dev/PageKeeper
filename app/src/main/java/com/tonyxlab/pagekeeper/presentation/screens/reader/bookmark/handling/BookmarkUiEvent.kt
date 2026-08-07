package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface BookmarkUiEvent : UiEvent{
    data object AddBookmark: BookmarkUiEvent
    data object SaveBookmark: BookmarkUiEvent
    data object DismissBookmarkDialog: BookmarkUiEvent
    data class ColorSelected(val color: BookmarkColor): BookmarkUiEvent
    data object NavigateBack: BookmarkUiEvent
}
