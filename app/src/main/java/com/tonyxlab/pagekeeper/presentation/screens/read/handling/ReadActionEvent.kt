package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface ReadActionEvent : ActionEvent {

    // Read ActionEvents
    data class ShowToast(val message: String) : ReadActionEvent

    data object ExitReader : ReadActionEvent

    data object NavigateToChaptersView : ReadActionEvent

    // Chapter ActionEvents
    data object CloseChapters : ReadActionEvent
}
