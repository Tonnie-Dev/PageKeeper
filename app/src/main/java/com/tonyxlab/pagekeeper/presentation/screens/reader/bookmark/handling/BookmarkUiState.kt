package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.BookmarkUiItem

data class BookmarkUiState(
        val bookMarks:List<BookmarkUiItem> = emptyList(),
        val showBookmarkDialog: Boolean = false
) : UiState
