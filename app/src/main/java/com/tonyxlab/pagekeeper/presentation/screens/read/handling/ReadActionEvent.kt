package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface ReadActionEvent : ActionEvent {
    data class ShowToast(val message: String) : ReadActionEvent
    data object ExitReader: ReadActionEvent
}
