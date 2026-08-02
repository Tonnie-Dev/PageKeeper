package com.tonyxlab.pagekeeper.presentation.screens.reader

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent


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

    data class ReadingPositionChanged(val blockIndex: Int) : ReaderUiEvent

    data object ViewChapters : ReaderUiEvent

    data object ViewBookmarks : ReaderUiEvent


    data object ChaptersJumpConsumed : ReaderUiEvent

    // Chapters UiEvents
    data object BackClicked: ReaderUiEvent
    data class ChapterSelected(val startBlockIndex: Int): ReaderUiEvent
    data class SectionClicked(val sectionIndex: Int): ReaderUiEvent
}
