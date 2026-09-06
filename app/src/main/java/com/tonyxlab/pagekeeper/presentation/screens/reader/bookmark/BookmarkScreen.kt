package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppButton
import com.tonyxlab.pagekeeper.presentation.core.components.AppDialog
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.core.components.LazyListComponent
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
            actionEventHandler = { _, action ->

                when (action) {
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

    val maxWidth = if (isDeviceWide) MAX_WIDTH else Dp.Unspecified

    uiState.bookmarkUiState.bookmarkUiItems.ifEmpty {
        EmptyBookmarkScreen(isDeviceWide = isDeviceWide)
    }

    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
    ) {
        LazyListComponent(
                modifier = Modifier.widthIn(max = maxWidth),
                items = bookmarkState.bookmarkUiItems,
                key = { it.id },
                content = { bookmark ->
                    BookmarkItem(
                            bookmarkUiItem = bookmark,
                            selected =uiState.bookmarkUiState.selectedMenuItemId == bookmark.id,
                            isMenuExpanded = uiState.bookmarkUiState.selectedMenuItemId == bookmark.id,
                            onBookmarkClicked = { onEvent(ReaderUiEvent.OnClickBookmark(bookmark)) },
                            onEditClick = { onEvent(ReaderUiEvent.EditBookmark(bookmark)) },
                            onDeleteClick = { onEvent(ReaderUiEvent.DeleteBookmarkClicked(bookmark)) },
                            onOpenMenu = { onEvent(ReaderUiEvent.ShowPopupMenu(bookmark)) },
                            onDismissMenu = { onEvent(ReaderUiEvent.DismissPopupMenu) }
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
            DeleteDialog(onEvent = onEvent)
        }
    }
}

@Composable
private fun DeleteDialog(
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

private val MAX_WIDTH = 600.dp


