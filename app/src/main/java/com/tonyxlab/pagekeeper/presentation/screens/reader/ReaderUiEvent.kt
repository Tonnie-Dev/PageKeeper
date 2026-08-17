package com.tonyxlab.pagekeeper.presentation.screens.reader

import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.BookmarkUiItem

sealed interface ReaderUiEvent : UiEvent {

    // Read UiEvents
    data object ToggleAutoRotate : ReaderUiEvent

    data object ToggleFavorite : ReaderUiEvent

    data object ExitReader : ReaderUiEvent

    data object ReadingAreaClicked : ReaderUiEvent

    data object FontSizeClicked : ReaderUiEvent

    data class PreviewFontSizeChange(val fontSize: Float) : ReaderUiEvent

    data object FontSizeChangeFinished : ReaderUiEvent

    data object IncreaseFontSize : ReaderUiEvent

    data object DecreaseFontSize : ReaderUiEvent

    data class ReadingPositionChanged(
        val blockIndex: Int,
        val textOffset: Int
    ) : ReaderUiEvent

    data object ViewChapters : ReaderUiEvent

    data object ViewBookmarks : ReaderUiEvent

    data object ChaptersJumpConsumed : ReaderUiEvent

    // Chapters UiEvents
    data object BackClicked : ReaderUiEvent

    data class ChapterSelected(val startBlockIndex: Int) : ReaderUiEvent

    data class SectionClicked(val sectionIndex: Int) : ReaderUiEvent

    // Bookmark UiEvents
    data object AddBookmark : ReaderUiEvent

    data object SaveBookmark : ReaderUiEvent

    data object DismissBookmarkDialog : ReaderUiEvent

    data class ColorSelected(val color: BookmarkColor) : ReaderUiEvent

    data class SelectBookmark(val bookmark: BookmarkUiItem) : ReaderUiEvent

    data class ShowBookmarkMenu(val bookmarkUiItem: BookmarkUiItem) : ReaderUiEvent

    data object NavigateBack : ReaderUiEvent

    data class EditBookmark(val bookmark: BookmarkUiItem) : ReaderUiEvent

    data class DeleteBookmark(val bookmark: BookmarkUiItem) : ReaderUiEvent
}