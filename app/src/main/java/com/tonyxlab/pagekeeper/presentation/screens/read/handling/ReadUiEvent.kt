package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface ReadUiEvent : UiEvent {
    data object ToggleAutoRotate : ReadUiEvent
    data object ChangeFontSize : ReadUiEvent
}