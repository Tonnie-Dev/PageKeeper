package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import com.tonyxlab.pagekeeper.domain.repository.BookmarkRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.toGlobalBookmarkUiItem
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.cancellation.CancellationException

typealias BookmarksBaseViewModel = BaseViewModel<BookmarksUiState, BookmarksUiEvent, BookmarksActionEvent>

class BookmarksViewModel(
    private val repository: BookmarkRepository
) : BookmarksBaseViewModel(initialState = BookmarksUiState()) {

    init {
        observeBooksWithBookmarks()
    }

    override fun onEvent(event: BookmarksUiEvent) {
        when (event) {
            is BookmarksUiEvent.OpenBook -> onBookClicked(event.bookId)
            BookmarksUiEvent.CancelDeleteDialog -> onCancelDeleteDialog()
            BookmarksUiEvent.ConfirmDelete -> onConfirmDeleteBookmarks()
            is BookmarksUiEvent.ContextMenuClicked -> onClickContextMenu(event.bookId)
            is BookmarksUiEvent.DeleteBookmarks -> onDeleteBookmarks()
            BookmarksUiEvent.DismissContextMenu -> closeContextMenu()
            is BookmarksUiEvent.ViewBookmarks -> onViewBookmarks(event.bookId)
            BookmarksUiEvent.ClearSearchClicked -> {}
            BookmarksUiEvent.ExitSearch -> {}
            BookmarksUiEvent.SearchClicked -> {}
            BookmarksUiEvent.ImportBook -> {}
        }
    }

    private fun observeBooksWithBookmarks() {

        launch {
            repository
                    .observeBooksWithBookmarks()
                    .collect { books ->
                        updateState { state ->
                            state.copy(
                                    globalBookmarkUiItems =
                                        books.map { it.toGlobalBookmarkUiItem() }
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

    private fun onViewBookmarks(bookId: String) {
        sendActionEvent(BookmarksActionEvent.NavigateToBookmarkPage(bookId))
        closeContextMenu()
    }

    private fun onDeleteBookmarks() {

        updateState { state ->
            state.copy(
                    deleteDialogState = state.deleteDialogState.copy(
                            showDeleteDialog = true
                    )
            )
        }
    }

    private fun onCancelDeleteDialog() {

        closeDeleteDialog()
    }

    private fun onConfirmDeleteBookmarks() {
        launch(context = Dispatchers.IO) {
            try {
                val selectedItemId = currentState.selectedMenuItemId ?: return@launch
                repository.deleteAllBookmarksForBook(bookId = selectedItemId)
                closeDeleteDialog()

            } catch (e: CancellationException) {
                throw e
            } catch (_: Throwable) {
                sendActionEvent(BookmarksActionEvent.ShowToast("Failed to delete bookmarks"))
            }
        }
    }

    private fun closeContextMenu() {
        updateState { state -> state.copy(selectedMenuItemId = null) }
    }

    private fun closeDeleteDialog() {
        updateState { state ->

            state.copy(deleteDialogState = state.deleteDialogState.copy(showDeleteDialog = false))
        }
    }
}

