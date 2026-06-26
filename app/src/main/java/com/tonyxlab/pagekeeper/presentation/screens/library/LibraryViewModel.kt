package com.tonyxlab.pagekeeper.presentation.screens.library

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.tonyxlab.pagekeeper.data.importer.BookImporter
import com.tonyxlab.pagekeeper.domain.ImportBookResult
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialog
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialogType
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDrawerDestination
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.SearchHandler
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.SelectionHandler
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart

typealias HomeBaseViewModel = BaseViewModel<LibraryUiState, LibraryUiEvent, LibraryActionEvent>

class LibraryViewModel(
    private val bookRepository: BookRepository,
    private val bookImporter: BookImporter,
) : HomeBaseViewModel(initialState = LibraryUiState()) {

    private val searchHandler = SearchHandler(
            bookRepository = bookRepository,
            coroutineScope = viewModelScope,
            currentState = { currentState },
            updateState = ::updateState
    )

    private val selectionHandler = SelectionHandler(
            currentState = { currentState },
            updateState = ::updateState
    )

    init {
        observeBooks()
        searchHandler.observeSearchQuery()
    }

    override fun onEvent(event: LibraryUiEvent) {
        when (event) {
            is LibraryUiEvent.OpenBook -> openBook(event.bookId)
            is LibraryUiEvent.FinishBook -> onFinishBook(event.bookId)
            is LibraryUiEvent.ConfirmDeleteDialog -> onConfirmDeleteDialog(event.bookId)
            is LibraryUiEvent.DeleteBook -> onDelete(event.bookId)
            LibraryUiEvent.DismissDialog -> dismissDialog()
            is LibraryUiEvent.MarkFavorite -> onMarkFavorite(event.bookId)
            is LibraryUiEvent.FileSelected -> onFileSelected(event.uri, event.fileName)
            LibraryUiEvent.ImportBookClicked -> onImport()
            is LibraryUiEvent.DrawerDestinationClicked -> onDrawerDestinationClicked(event.destination)
            is LibraryUiEvent.ShareBook -> onShareBook(event.bookId)
            LibraryUiEvent.ClearSearchClicked -> searchHandler.clearSearchText()
            LibraryUiEvent.SearchBackClicked -> searchHandler.exitSearchMode()
            LibraryUiEvent.SearchClicked -> searchHandler.enterSearchMode()
            LibraryUiEvent.AddSelectedToFavoritesClicked -> onAddSelectedToFavorites()
            is LibraryUiEvent.BookLongClicked -> selectionHandler.enterSelectionMode(event.bookId)
            is LibraryUiEvent.BookSelectionToggled -> selectionHandler.toggleBookSelection(event.bookId)
            LibraryUiEvent.CancelDeleteSelectedClicked -> dismissDialog()
            LibraryUiEvent.ConfirmDeleteSelectedClicked -> onDeleteSelected()
            LibraryUiEvent.DeleteSelectedClicked -> onConfirmDeleteSelectedDialog()
            LibraryUiEvent.ExitSelectionModeClicked -> selectionHandler.exitSelectionMode()
            LibraryUiEvent.ShareSelectedClicked -> onShareSelected()
        }
    }

    private fun observeBooks() {
        launch {
            bookRepository.observeBooks()
                    .onStart {
                        updateState { it.copy(isLoading = true) }
                    }
                    .catch {
                        updateState { state -> state.copy(isLoading = false) }
                        showToast("Unable to load books.")
                    }
                    .collectLatest { books ->
                        updateState { it.copy(books = books, isLoading = false) }
                    }
        }
    }

    private fun openBook(bookId: String) {
        sendActionEvent(LibraryActionEvent.OpenBook(bookId))
    }

    private fun onFinishBook(bookId: String) {
        val book = currentState.books.firstOrNull { it.id == bookId } ?: return
        launchCatching(
                onError = { showToast("Unable to update book status.") }
        ) {
            bookRepository.updateFinished(bookId, !book.isFinished)
        }
    }

    private fun onConfirmDeleteDialog(bookId: String) {
        val book = currentState.books.firstOrNull { it.id == bookId } ?: return
        updateState {
            it.copy(
                    dialog = LibraryDialog(
                            title = "Delete \"${book.title}\"?",
                            message = "This action will remove the book from your library.",
                            positiveButtonText = "Delete",
                            negativeButtonText = "Cancel",
                            type = LibraryDialogType.DeleteBook,
                            bookId = bookId
                    )
            )
        }
    }

    private fun onDelete(bookId: String) {
        currentState.books.firstOrNull { it.id == bookId } ?: return
        launchCatching(
                onError = { showToast("Unable to delete book.") },
                onCompletion = { dismissDialog() }
        ) {
            bookRepository.deleteBookById(id = bookId)
        }
    }

    private fun dismissDialog() {
        updateState { it.copy(dialog = null) }
    }

    private fun onMarkFavorite(bookId: String) {
        val book = currentState.books.firstOrNull { it.id == bookId } ?: return
        launchCatching(
                onError = { showToast("Unable to update favorite status.") }
        ) {
            bookRepository.updateFavorite(bookId, !book.isFavorite)
        }
    }

    private fun onFileSelected(uri: Uri, fileName: String) {
        if (!fileName.endsWith(FB2_EXTENSION, ignoreCase = true)) {
            showUnsupportedFileDialog()
            return
        }

        launchCatching(
                onStart = { updateState { it.copy(isImporting = true) } },
                onError = { showToast("Unable to import book.") },
                onCompletion = { updateState { it.copy(isImporting = false) } }
        ) {
            when (val result = bookImporter.importBook(uri)) {
                ImportBookResult.Success -> showToast("Book imported.")
                ImportBookResult.Duplicate -> showToast("This book is already in your library.")
                ImportBookResult.UnsupportedFormat -> showUnsupportedFileDialog()
                ImportBookResult.Loading -> updateState { it.copy(isImporting = true) }
                is ImportBookResult.Error -> showToast(result.message)
            }
        }
    }

    private fun onImport() {
        sendActionEvent(LibraryActionEvent.OpenFilePicker)
    }

    private fun onShareBook(bookId: String) {
        sendActionEvent(LibraryActionEvent.ShareBook(bookId))
    }

    private fun onAddSelectedToFavorites() {
        val selectedBookIds = selectionHandler.getSelectedBookIds()
        selectedBookIds.ifEmpty { return }

        launchCatching(
                onError = { showToast("Unable to update selected books.") },
                onCompletion = { selectionHandler.exitSelectionMode() }
        ) {
            selectedBookIds.forEach { bookId ->
                bookRepository.updateFavorite(bookId, true)
            }
        }
    }

    private fun onShareSelected() {
        val selectedBookIds = selectionHandler.getSelectedBookIds()
        val bookId = selectedBookIds.singleOrNull()

        if (bookId == null) {
            showToast("Select one book to share.")
            return
        }

        selectionHandler.exitSelectionMode()
        sendActionEvent(LibraryActionEvent.ShareBook(bookId))
    }

    private fun onConfirmDeleteSelectedDialog() {
        val selectedCount = currentState.selectionState.selectedCount
        if (selectedCount == 0) return

        updateState {
            it.copy(
                    dialog = LibraryDialog(
                            title = "Delete selected books?",
                            message = "This action will remove $selectedCount selected book(s) from your library.",
                            positiveButtonText = "Delete",
                            negativeButtonText = "Cancel",
                            type = LibraryDialogType.DeleteSelectedBooks
                    )
            )
        }
    }

    private fun onDeleteSelected() {
        val selectedBookIds = selectionHandler.getSelectedBookIds()
        if (selectedBookIds.isEmpty()) return

        launchCatching(
                onError = { showToast("Unable to delete selected books.") },
                onCompletion = {
                    dismissDialog()
                    selectionHandler.exitSelectionMode()
                }
        ) {
            selectedBookIds.forEach { bookId ->
                bookRepository.deleteBookById(id = bookId)
            }
        }
    }

    private fun onDrawerDestinationClicked(destination: LibraryDrawerDestination) {
        updateState { it.copy(selectedDrawerDestination = destination) }
    }

    private fun showUnsupportedFileDialog() {
        updateState {
            it.copy(
                    dialog = LibraryDialog(
                            title = "Unsupported file format",
                            message = "Only FB2 books can be imported.",
                            positiveButtonText = "OK",
                            type = LibraryDialogType.UnsupportedFile
                    )
            )
        }
    }

    private fun showToast(message: String) {
        sendActionEvent(LibraryActionEvent.ShowToast(message))
    }

    private companion object {
        const val FB2_EXTENSION = ".fb2"
    }
}
