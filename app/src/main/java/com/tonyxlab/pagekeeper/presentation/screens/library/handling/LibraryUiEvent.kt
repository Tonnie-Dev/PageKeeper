package com.tonyxlab.pagekeeper.presentation.screens.library.handling

import android.net.Uri
import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface LibraryUiEvent: UiEvent{

    data object ImportBookClicked : LibraryUiEvent

    data class OpenBook(val bookId: String) : LibraryUiEvent

    data class MarkFavorite(val bookId: String) : LibraryUiEvent

    data class FinishBook(val bookId: String) : LibraryUiEvent

    data class ShareBook(val bookId: String) : LibraryUiEvent

    data class DeleteBook(val bookId: String) : LibraryUiEvent

    data class ConfirmDeleteDialog(val bookId: String) : LibraryUiEvent

    data object DismissDialog : LibraryUiEvent

    data class FileSelected(val uri: Uri, val fileName: String) : LibraryUiEvent

    data object SearchClicked : LibraryUiEvent

    data object SearchBackClicked : LibraryUiEvent

    data object ClearSearchClicked : LibraryUiEvent

    data class SearchQueryChanged(val query: String) : LibraryUiEvent
}
