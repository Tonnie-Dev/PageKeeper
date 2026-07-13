package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface ReadUiEvent : UiEvent {
    data object ToggleAutoRotate : ReadUiEvent

    data object ToggleFavorite : ReadUiEvent

    data object ExitReader : ReadUiEvent

    data object ToggleImmersiveMode : ReadUiEvent

    data object FontSizeClicked : ReadUiEvent

    data class PreviewFontSizeChange(val fontSizeSp: Float) : ReadUiEvent

    data class FontSizeChangeFinished(val fontSizeSp: Float) : ReadUiEvent

    data object IncreaseFontSize : ReadUiEvent

    data object DecreaseFontSize : ReadUiEvent



}
