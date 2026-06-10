package com.tonyxlab.pagekeeper.presentation.screens.library.handling

import com.tonyxlab.pagekeeper.presentation.core.handling.ActionEvent

sealed interface LibraryActionEvent : ActionEvent {

    data class OpenBook(val bookId: Long) : LibraryActionEvent

    data class ShareBook(val bookId: Long) : LibraryActionEvent

    data object OpenFilePicker : LibraryActionEvent

    data class ShowToast(val message: String) : LibraryActionEvent
}