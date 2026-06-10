package com.tonyxlab.pagekeeper.presentation.screens.library.handling

import android.net.Uri
import com.tonyxlab.pagekeeper.presentation.core.handling.UiEvent

sealed interface LibraryUiEvent: UiEvent{

    data object ImportBookClicked : LibraryUiEvent

    data class BookSelected(val bookId: Long) : LibraryUiEvent

    data class FavoriteBookClicked(val bookId: Long) : LibraryUiEvent

    data class BookmarkBookClicked(val bookId: Long) : LibraryUiEvent

    data class ShareBookClicked(val bookId: Long) : LibraryUiEvent

    data class DeleteBookClicked(val bookId: Long) : LibraryUiEvent

    data class ConfirmDeleteBook(val bookId: Long) : LibraryUiEvent

    data object DismissDialog : LibraryUiEvent

    data class FileSelected(val uri: Uri, val fileName: String) : LibraryUiEvent
}