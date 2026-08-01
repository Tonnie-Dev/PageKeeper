package com.tonyxlab.pagekeeper.presentation.screens.reader

import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface ReaderActionEvent : ActionEvent {

    // Read ActionEvents
    data class ShowToast(val message: String) : ReaderActionEvent

    data object ExitReader : ReaderActionEvent

    data object NavigateToChaptersView : ReaderActionEvent

    // Chapter ActionEvents
    data object CloseChapters : ReaderActionEvent
}
