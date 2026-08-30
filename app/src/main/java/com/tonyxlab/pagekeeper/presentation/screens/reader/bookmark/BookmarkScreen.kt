package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark

import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppButton
import com.tonyxlab.pagekeeper.presentation.core.components.AppDialog
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.core.components.LazyListComponent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components.BookmarkDialog
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components.BookmarkItem
import com.tonyxlab.pagekeeper.utils.rememberIsDeviceWide

@Composable
fun BookmarkScreen(
    viewModel: ReadViewModel,
    navigateToReadScreen: () -> Unit
) {
    BaseContentLayout(
            viewModel = viewModel,
            topBar = {

                AppTopBar(
                        titleText = stringResource(id = R.string.topbar_text_bookmarks),
                        backgroundColor = MaterialTheme.colorScheme.background,
                        onNavButtonClick = navigateToReadScreen
                )
            },
            actionEventHandler = {_,action ->

                when(action) {
                    ReaderActionEvent.ExitBookmark -> navigateToReadScreen()
                   else -> Unit
                }


            },
            floatingActionButton = {
                AppButton(
                        modifier = Modifier.navigationBarsPadding(),
                        buttonText = stringResource(id = R.string.button_text_add_bookmark),
                        leadingIcon = painterResource(R.drawable.ic_bookmark_add),
                        onClick = { viewModel.onEvent(ReaderUiEvent.AddBookmark) }
                )
            }
    ) { state ->

        BookmarkScreenContent(
                uiState = state,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun BookmarkScreenContent(
    uiState: ReaderUiState,
    onEvent: (ReaderUiEvent) -> Unit
) {
    val isDeviceWide = rememberIsDeviceWide()
    val bookmarkState = uiState.bookmarkUiState
    val selectedBookmarkId = bookmarkState.selectedBookmarkId

    val dialogInputState = uiState.bookmarkUiState.dialogInputState
    uiState.bookmarkUiState.bookmarkUiItems.ifEmpty {
        EmptyBookmarkScreen(isDeviceWide = isDeviceWide)
    }

    LazyListComponent(
            modifier = Modifier,
            items = bookmarkState.bookmarkUiItems,
            key = { it.id },
            content = { bookmark ->
                BookmarkItem(
                        bookmarkUiItem = bookmark,
                        onBookmarkClicked = { onEvent(ReaderUiEvent.OnClickBookmark(bookmark)) },
                        selected = bookmark.id == selectedBookmarkId,
                        onEditClick = { onEvent(ReaderUiEvent.EditBookmark(bookmark)) },
                        onDeleteClick = { onEvent(ReaderUiEvent.DeleteBookmarkClicked(bookmark)) },
                        onOpenMenu = { onEvent(ReaderUiEvent.ShowPopupMenu(bookmark)) },
                        onDismissMenu = { onEvent(ReaderUiEvent.DismissPopupMenu) },
                        isMenuExpanded = selectedBookmarkId == bookmark.id
                )
            }
    )

    if (dialogInputState.showEditBookmarkDialog) {
        BookmarkDialog(
                modifier = Modifier,
                textFieldState = dialogInputState.textFieldState,
                selectedColor = dialogInputState.selectedColor,
                onColorSelected = { color ->
                    onEvent(ReaderUiEvent.ColorSelected(color))
                },
                onDismissRequest = {
                    onEvent(ReaderUiEvent.DismissBookmarkDialog)
                },
                onSave = {
                    onEvent(ReaderUiEvent.SaveBookmark)
                }
        )
    }


    if (dialogInputState.showDeleteBookmarkDialog) {
       BookmarkDialog (onEvent = onEvent)
    }
}




@Composable
private fun BookmarkDialog(
    onEvent: (ReaderUiEvent) -> Unit
) {
    AppDialog(
            dialogTitle = stringResource(id = R.string.dialog_text_delete_bookmark),
            dialogText = stringResource(id = R.string.dialog_text_remove_bookmark),
            positiveButtonText = stringResource(id = R.string.dialog_text_delete),
            negativeButtonText = stringResource(id = R.string.dialog_text_cancel),
            isDeleteDialog = true,
            onDismissRequest = { onEvent(ReaderUiEvent.CancelDeleteBookmark) },
            onConfirm = {
                onEvent(ReaderUiEvent.ConfirmDeleteBookmark)
            }
    )
}


