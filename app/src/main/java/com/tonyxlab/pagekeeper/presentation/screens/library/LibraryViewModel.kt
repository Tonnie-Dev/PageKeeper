@file:OptIn(FlowPreview::class)

package com.tonyxlab.pagekeeper.presentation.screens.library

import android.net.Uri
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import com.tonyxlab.pagekeeper.data.importer.BookImporter
import com.tonyxlab.pagekeeper.domain.ImportBookResult
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialog
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialogType
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlin.time.Duration.Companion.milliseconds

typealias HomeBaseViewModel = BaseViewModel<LibraryUiState, LibraryUiEvent, LibraryActionEvent>

class LibraryViewModel(
    private val bookRepository: BookRepository,
    private val bookImporter: BookImporter,
) : HomeBaseViewModel(initialState = LibraryUiState()) {

    init {
        observeBooks()
        observeSearchQuery()
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
            is LibraryUiEvent.ShareBook -> onShareBook(event.bookId)
            LibraryUiEvent.ClearSearchClicked -> clearSearchText()
            LibraryUiEvent.SearchBackClicked -> onSearchBackClick()
            LibraryUiEvent.SearchClicked -> onSearch()

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

    private fun observeSearchQuery() {

        val textFlow = snapshotFlow {
            currentState.searchState.searchTextFieldState.text
        }

        textFlow.debounce(300.milliseconds)
                .map { it.toString() }
                .distinctUntilChanged()
                .onEach { query ->
                    onSearchQueryChange(query = query)
                }
                .launchIn(viewModelScope)

    }

    private fun onSearchQueryChange(query: String) {
        val cleanQuery = query.trim()

        if (cleanQuery.isBlank()) {
            updateState {
                it.copy(
                        searchState = it.searchState.copy(
                                searchResults = emptyList()
                        )
                )
            }
            return
        }

        launch {
            val searchResults = bookRepository.searchBooks(cleanQuery)
                    .first()

            updateState {
                it.copy(
                        searchState = it.searchState.copy(
                                searchResults = searchResults
                        )
                )
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
        val book = currentState.books.firstOrNull { it.id == bookId } ?: return
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

    private fun onSearch() {
        updateState {
            it.copy(
                    searchState = it.searchState.copy(
                            isSearchMode = true
                    )
            )
        }
    }

    private fun onSearchBackClick() {
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

    private fun clearSearchText() {
        currentState.searchState.searchTextFieldState.edit {
            replace(0, length, "")
        }
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
