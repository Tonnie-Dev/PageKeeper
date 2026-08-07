package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling

import androidx.compose.foundation.text.input.TextFieldState
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.BookmarkUiItem

data class BookmarkUiState(
    val bookMarks: List<BookmarkUiItem> = emptyList(),
    val dialogInputState: DialogInputState = DialogInputState()
) : UiState {

    data class DialogInputState(
        val textFieldState: TextFieldState = TextFieldState(),
        val showBookmarkDialog: Boolean = false,
        val selectedColor: BookmarkColor = BookmarkColor.Blue
    )

}
