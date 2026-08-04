package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark

import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppButton
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components.BookmarkDialog
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.handling.BookmarkUiState
import com.tonyxlab.pagekeeper.utils.rememberIsDeviceWide
import org.koin.androidx.compose.koinViewModel

@Composable
fun BookmarkScreen(
    bookId: String,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    viewModel: BookmarkViewModel = koinViewModel()
) {

    BaseContentLayout(
            viewModel = viewModel,
            topBar = {

                AppTopBar(
                        titleText = stringResource(id = R.string.topbar_text_bookmarks),
                        backgroundColor = MaterialTheme.colorScheme.background,
                        onNavButtonClick = {},
                        actionIcon = {}
                )
            },
            floatingActionButton = {
                AppButton(
                        modifier = Modifier.navigationBarsPadding(),
                        buttonText = stringResource(id = R.string.button_text_add_bookmark),
                        leadingIcon = painterResource(R.drawable.ic_bookmark_add)
                ) { }
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
    uiState: BookmarkUiState,
    onEvent: (BookmarkUiEvent) -> Unit
) {
    val isDeviceWide = rememberIsDeviceWide()
    uiState.bookMarks.ifEmpty {

        EmptyBookmarkScreen(isDeviceWide = isDeviceWide)
    }

    BookmarkDialog(

            modifier = Modifier,
            title = "Title",
            selectedColor = BookmarkColor.Blue,
            onTitleChange = {},
            onColorSelected = {},
            onDismissRequest = {},
            onSave = {},
    )
}