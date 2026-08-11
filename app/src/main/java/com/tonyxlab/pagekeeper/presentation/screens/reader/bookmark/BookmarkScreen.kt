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
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.core.components.LazyListComponent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadViewModel
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

    uiState.bookmarkUiState.bookMarks.ifEmpty {
        EmptyBookmarkScreen(isDeviceWide = isDeviceWide)
    }

    val dialogInputState = uiState.bookmarkUiState.dialogInputState


    LazyListComponent(
            modifier = Modifier,
            items = uiState.bookmarkUiState.bookMarks,
            key = { it.id },
            content = { bookmark ->
                BookmarkItem(
                        bookmark = bookmark,
                        onClick = { onEvent(ReaderUiEvent.SelectBookmark(bookmark)) },
                        onMenuClick = { onEvent(ReaderUiEvent.ShowBookmarkMenu(bookmark)) }
                )
            }
    )

    if (dialogInputState.showBookmarkDialog) {
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
                },
        )
    }
}