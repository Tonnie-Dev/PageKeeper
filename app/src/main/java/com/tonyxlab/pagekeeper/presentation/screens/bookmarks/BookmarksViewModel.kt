package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import android.net.Uri
import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.data.importer.BookImporter
import com.tonyxlab.pagekeeper.domain.ImportBookResult
import com.tonyxlab.pagekeeper.domain.repository.BookmarkRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.core.components.SnackbarType
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent.ShowSnackbar
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksDialogState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksDialogType
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.toGlobalBookmarkUiItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.coroutines.cancellation.CancellationException

typealias BookmarksBaseViewModel = BaseViewModel<BookmarksUiState, BookmarksUiEvent, BookmarksActionEvent>

class BookmarksViewModel(
    private val repository: BookmarkRepository,
    private val bookImporter: BookImporter
) : BookmarksBaseViewModel(initialState = BookmarksUiState()) {

    init {
        observeBooksWithBookmarks()
        searchBookmarks()
    }

    override fun onEvent(event: BookmarksUiEvent) {
        when (event) {
            is BookmarksUiEvent.OpenBook -> onBookClicked(event.bookId)
            BookmarksUiEvent.CancelDeleteDialog -> onCancelDeleteDialog()
            BookmarksUiEvent.ConfirmDelete -> onConfirmDeleteBookmarks()
            is BookmarksUiEvent.ContextMenuClicked -> onClickContextMenu(event.bookId)
            is BookmarksUiEvent.DeleteBookmarks -> onDeleteBookmarks(
                    bookId = event.bookId,
                    dialogTitle = event.dialogTitle,
                    dialogMessage = event.dialogMessage,
                    positiveButtonText = event.positiveButtonText,
                    negativeButtonText = event.negativeButtonText
            )

            BookmarksUiEvent.DismissContextMenu -> closeContextMenu()
            is BookmarksUiEvent.ViewBookmarks -> onViewBookmarks(event.bookId)
            BookmarksUiEvent.ClearSearchClicked -> clearSearchText()
            BookmarksUiEvent.ExitSearch -> exitSearchMode()
            BookmarksUiEvent.SearchClicked -> enterSearchMode()
            BookmarksUiEvent.ImportBook -> onImport()
            is BookmarksUiEvent.FileSelected -> onFileSelected(event.uri, event.fileName)
            BookmarksUiEvent.ViewLibrary -> viewLibrary()
        }
    }

    private fun observeBooksWithBookmarks() {
        launch {
            repository
                    .observeBooksWithBookmarks()
                    .collect { books ->

                        val items = books.map {
                            it.toGlobalBookmarkUiItem()
                        }
                        updateState { state ->
                            state.copy(
                                    globalBookmarkUiItems = items
                            )
                        }
                    }
        }
    }

    private fun onBookClicked(bookId: String) {
        sendActionEvent(BookmarksActionEvent.NavigateToBookmarkPage(bookId))
    }

    private fun onClickContextMenu(bookId: String) {
        updateState { state ->
            state.copy(
                    selectedMenuItemId = bookId
            )
        }
    }

    private fun onImport() {
        sendActionEvent(BookmarksActionEvent.OpenFilePicker)
    }

    private fun onFileSelected(uri: Uri, fileName: String) {

        launchCatching(
                onStart = { updateState { it.copy(isImporting = true) } },
                onError = { showSnackbar(SnackbarType.Error) },
                onCompletion = { updateState { it.copy(isImporting = false) } }
        ) {
            when (val result = bookImporter.importBook(uri)) {
                ImportBookResult.Success -> showSnackbar(SnackbarType.Success)
                ImportBookResult.Duplicate -> showSnackbar(SnackbarType.Duplicate)
                ImportBookResult.UnsupportedFormat -> showUnsupportedFileDialog()
                ImportBookResult.Loading -> updateState { it.copy(isImporting = true) }
                is ImportBookResult.Error -> showSnackbar(SnackbarType.Error)
            }
        }
    }

    private fun enterSearchMode() {
        updateState {
            it.copy(
                    searchState = it.searchState.copy(
                            isSearchMode = true
                    )
            )
        }
    }

    private fun searchBookmarks() {
        launch {
            snapshotFlow {
                currentState.searchState.searchTextFieldState.text
            }
                    .map {
                        it.toString()
                                .trim()
                    }
                    .distinctUntilChanged()
                    .collect {
                        filterBookmarks(it)
                    }
        }
    }

    private fun filterBookmarks(text: String) {

        val searchResults =
            if (text.isBlank()) {
                emptyList()
            } else {
                currentState.globalBookmarkUiItems.filter { bookmark ->
                    bookmark.title.contains(text, ignoreCase = true) ||
                            bookmark.author.contains(text, ignoreCase = true)
                }
            }

        updateState { state ->
            state.copy(
                    searchState = state.searchState.copy(
                            searchResults = searchResults
                    )
            )
        }
    }

    private fun clearSearchText() {
        currentState.searchState.searchTextFieldState.edit {
            replace(0, length, "")
        }
    }

    private fun exitSearchMode() {
        clearSearchText()
        updateState {
            it.copy(
                    searchState = it.searchState.copy(
                            isSearchMode = false,
                            searchResults = emptyList()
                    )
            )
        }
    }

    private fun onViewBookmarks(bookId: String) {
        sendActionEvent(BookmarksActionEvent.NavigateToBookmarkPage(bookId))
        closeContextMenu()
    }

    private fun onDeleteBookmarks(
        bookId: String,
        dialogTitle: String,
        dialogMessage: String,
        positiveButtonText: String,
        negativeButtonText: String
    ) {
        updateState { state ->
            state.copy(
                    bookmarkDialogState = BookmarksDialogState(
                            title = dialogTitle,
                            message = dialogMessage,
                            positiveButtonText = positiveButtonText,
                            negativeButtonText = negativeButtonText,
                            type = BookmarksDialogType.DeleteBookmarks,
                            bookId = bookId
                    ),
                    selectedMenuItemId = null
            )
        }
    }

    private fun onConfirmDeleteBookmarks() {

        val bookId = currentState.bookmarkDialogState?.bookId ?: return

        launch(context = Dispatchers.IO) {
            try {
                repository.deleteAllBookmarksForBook(bookId = bookId)
                updateState { it.copy(bookmarkDialogState = null) }

            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                showSnackbar(SnackbarType.Error)
            }
        }
    }

    private fun onCancelDeleteDialog() {
        updateState { it.copy(bookmarkDialogState = null) }
    }

    private fun closeContextMenu() {
        updateState { state -> state.copy(selectedMenuItemId = null) }
    }

    private fun showUnsupportedFileDialog() {
        updateState {
            it.copy(
                    bookmarkDialogState = BookmarksDialogState(
                            title = "Unsupported file format",
                            message = "Only FB2 books can be imported.",
                            positiveButtonText = "OK",
                            type = BookmarksDialogType.UnsupportedFile
                    )
            )
        }
    }

    private fun showSnackbar(snackbarType: SnackbarType) {
        when (snackbarType) {
            SnackbarType.Success -> sendActionEvent(
                    ShowSnackbar(
                            messageRes = R.string.snack_text_book_imported,
                            actionLabelRes = R.string.snack_text_view_library,
                            event = BookmarksUiEvent.ViewLibrary
                    )
            )

            SnackbarType.Duplicate -> sendActionEvent(
                    ShowSnackbar(
                            messageRes = R.string.snack_text_book_already_in_library,
                            actionLabelRes = R.string.blank_text,
                    )
            )

            SnackbarType.Error -> sendActionEvent(
                    ShowSnackbar(
                            messageRes = R.string.snack_text_unable_to_import_book,
                            actionLabelRes = R.string.blank_text,
                            isError = true
                    )
            )
        }
    }

    private fun viewLibrary() {
        sendActionEvent(BookmarksActionEvent.NavigateToLibrary)
    }
}




