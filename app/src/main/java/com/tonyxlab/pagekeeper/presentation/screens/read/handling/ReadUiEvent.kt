package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent


sealed interface ReadUiEvent : UiEvent {

    // Read UiEvents
    data object ToggleAutoRotate : ReadUiEvent

    data object ToggleFavorite : ReadUiEvent

    data object ExitReader : ReadUiEvent

    data object ReadingAreaClicked : ReadUiEvent

    data object FontSizeClicked : ReadUiEvent

    data class PreviewFontSizeChange(val fontSize: Float) : ReadUiEvent

    data object FontSizeChangeFinished : ReadUiEvent

    data object IncreaseFontSize : ReadUiEvent

    data object DecreaseFontSize : ReadUiEvent

    data class ReadingPositionChanged(val blockIndex: Int) : ReadUiEvent

    data object ViewChapters : ReadUiEvent

    data object ChaptersJumpConsumed : ReadUiEvent

    // Chapters UiEvents
    data object BackClicked: ReadUiEvent
    data class ChapterSelected(val startBlockIndex: Int): ReadUiEvent
    data class SectionClicked(val sectionIndex: Int): ReadUiEvent
}
