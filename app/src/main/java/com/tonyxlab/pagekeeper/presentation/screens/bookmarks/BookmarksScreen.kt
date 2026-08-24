package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import androidx.compose.runtime.Composable
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.core.components.LazyListComponent
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components.BookmarkCard
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components.BookmarksTopBar
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookmarksScreen(
    navigator: Navigator,
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
            },
            actionEventHandler = {_,action -> when(action) {
                is BookmarksActionEvent.NavigateToBookmarkPage -> navigator.navigateToBookBookmarks(action.bookId)

                    is BookmarksActionEvent.ShowToast -> TODO()
            }

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

    LazyListComponent(
            items = uiState.globalBookmarkUiItems,
            key = { it.bookId }
    ) { item ->
        BookmarkCard(
                item = item,
                selected = uiState.selectedMenuItemId == item.bookId,
                isMenuExpanded = uiState.selectedMenuItemId == item.bookId,
                onItemClick = { onEvent(BookmarksUiEvent.BookClicked(item.bookId)) },
                onViewBookmarks = { onEvent(BookmarksUiEvent.ViewBookmarks(item.bookId)) },
                onDeleteBookmarks = { onEvent(BookmarksUiEvent.DeleteBookmarks(item.bookId)) },
                onOpenContextMenu = { onEvent(BookmarksUiEvent.ContextMenuClicked(item.bookId)) },
                onDismissMenu = { onEvent(BookmarksUiEvent.DismissContextMenu) }
        )
    }
}