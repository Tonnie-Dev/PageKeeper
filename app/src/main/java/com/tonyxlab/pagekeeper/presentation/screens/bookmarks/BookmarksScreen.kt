package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import androidx.compose.runtime.Composable
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components.BookmarksTopBar
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = koinViewModel(),
) {
    BaseContentLayout(
            viewModel = viewModel,
            topBar = {
                BookmarksTopBar(
                        showNavIcon = true,
                        onNavButtonClick = {},
                        onActionClick = {}
                )
            }
    ) { uiState ->

        BookmarksScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )

    }
}

@Composable
private fun BookmarksScreenContent(
    uiState: BookmarksUiState,
    onEvent: (BookmarksUiEvent) -> Unit
) {

    uiState.globalBookmarkUiItems.ifEmpty {

        EmptyBookmarkScreen(isGlobalScreen = true, isDeviceWide = false)

    }

}