package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface ReadUiEvent : UiEvent {
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


}
