package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface ReadUiEvent : UiEvent {
    data object ToggleAutoRotate : ReadUiEvent
    data object DecreaseFontSize : ReadUiEvent
    data object IncreaseFontSize : ReadUiEvent
    data object ChangeFontSize : ReadUiEvent
    data class SetFontSize(val fontSizeSp: Float) : ReadUiEvent
}
