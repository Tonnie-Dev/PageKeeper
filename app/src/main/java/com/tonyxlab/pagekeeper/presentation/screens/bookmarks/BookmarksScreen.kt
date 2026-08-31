package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppDialog
import com.tonyxlab.pagekeeper.presentation.core.components.AppNavigationDrawer
import com.tonyxlab.pagekeeper.presentation.core.components.AppNavigationRail
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.core.components.LazyListComponent
import com.tonyxlab.pagekeeper.presentation.core.components.SearchComponent
import com.tonyxlab.pagekeeper.presentation.core.components.WideDummySearchBar
import com.tonyxlab.pagekeeper.presentation.core.utils.rememberFilePicker
import com.tonyxlab.pagekeeper.presentation.navigation.AppNavigationDestination
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components.BookmarkCard
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components.BookmarksTopBar
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksDialogType
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.handling.BookmarksUiState
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.rememberIsDeviceWide
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import timber.log.Timber

@Composable
fun BookmarksScreen(
    navigator: Navigator,
    viewModel: BookmarksViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val inSearchMode = uiState.searchState.isSearchMode

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isNavigationRailExpanded by rememberSaveable { mutableStateOf(false) }

    val isDeviceWide = rememberIsDeviceWide()

    if (isDeviceWide) {

        Row(modifier = Modifier.fillMaxSize()) {

            AppNavigationRail(
                    selectedDestination = AppNavigationDestination.Bookmarks,
                    expanded = isNavigationRailExpanded,
                    onExpandedChange = { isNavigationRailExpanded = it },
                    onImportBookClick = { viewModel.onEvent(BookmarksUiEvent.ImportBook) },
                    onDestinationClick = { destination ->
                        when (destination) {
                            AppNavigationDestination.Bookmarks -> Unit
                            AppNavigationDestination.Library,
                            AppNavigationDestination.Favorites,
                            AppNavigationDestination.Finished -> {
                                navigator.navigateToLibrary(destination)
                            }
                        }
                    },
                    exitSearch = { viewModel.onEvent(BookmarksUiEvent.ExitSearch) }
            )
            Box(
                    modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
            ) {

                MergedLibraryLayout(
                        modifier = Modifier.padding(
                                top = MaterialTheme.spacing.spaceLarge,
                                start = MaterialTheme.spacing.spaceMedium,
                                end = MaterialTheme.spacing.spaceMedium,
                                bottom = MaterialTheme.spacing.spaceMedium
                        ),
                        showNavigationIcon = false,
                        showTopBar = false,
                        navigator = navigator,
                        onNavButtonClick = {},
                        viewModel = viewModel
                )
            }
        }

    } else {
        ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = inSearchMode.not(),
                scrimColor = Color.Black.copy(alpha = 0.38f),
                drawerContent = {
                    AppNavigationDrawer(
                            selectedDestination = AppNavigationDestination.Bookmarks,
                            onCloseClick = {
                                coroutineScope.launch { drawerState.close() }
                            },
                            onImportBookClick = {
                                coroutineScope.launch { drawerState.close() }
                                viewModel.onEvent(BookmarksUiEvent.ImportBook)
                            },
                            onDestinationClick = { destination ->

                                when (destination) {
                                    AppNavigationDestination.Bookmarks -> Unit
                                    AppNavigationDestination.Library,
                                    AppNavigationDestination.Favorites,
                                    AppNavigationDestination.Finished ->
                                        navigator.navigateToLibrary(destination)
                                }
                                coroutineScope.launch { drawerState.close() }
                            }
                    )
                }
        ) {
            MergedLibraryLayout(
                    showNavigationIcon = true,
                    showTopBar = true,
                    navigator = navigator,
                    viewModel = viewModel,
                    onNavButtonClick = {
                        coroutineScope.launch { drawerState.open() }
                    }
            )
        }
    }
}

@Composable
private fun MergedLibraryLayout(
    showNavigationIcon: Boolean,
    showTopBar: Boolean,
    onNavButtonClick: () -> Unit,
    navigator: Navigator,
    viewModel: BookmarksViewModel,
    modifier: Modifier = Modifier,
) {
    val isDeviceWide = rememberIsDeviceWide()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val inSearchMode = uiState.searchState.isSearchMode

    val filePicker = rememberFilePicker { uri, fileName ->
        viewModel.onEvent(BookmarksUiEvent.FileSelected(uri, fileName))
    }

    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                if (showTopBar && inSearchMode.not()) {
                    BookmarksTopBar(
                            showNavIcon = showNavigationIcon,
                            onNavButtonClick = onNavButtonClick,
                            onActionClick = { viewModel.onEvent(BookmarksUiEvent.SearchClicked) }
                    )
                }
            },

            actionEventHandler = { actionContext, actionEvent ->
                when (actionEvent) {

                    is BookmarksActionEvent.NavigateToBookmarkPage -> {

                        navigator.navigateToBookBookmarks(actionEvent.bookId)
                    }

                    is BookmarksActionEvent.ShowToast -> {
                        Toast.makeText(actionContext, actionEvent.message, Toast.LENGTH_SHORT)
                                .show()
                    }

                    BookmarksActionEvent.OpenFilePicker -> {
                        filePicker.launch("*/*")
                    }
                }
            }
    ) { state ->

        if (isDeviceWide) {
            WideBookmarksLayout(
                    modifier = Modifier,
                    uiState = state,
                    onEvent = viewModel::onEvent
            )
        } else {
            CompactBookmarksLayout(
                    modifier = Modifier,
                    uiState = state,
                    onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun WideBookmarksLayout(
    modifier: Modifier = Modifier,
    uiState: BookmarksUiState,
    onEvent: (BookmarksUiEvent) -> Unit
) {
    val isDeviceWide = rememberIsDeviceWide()
    val isSearchActive = uiState.searchState.isSearchMode

    val deleteAllBookmarksTitle =
        stringResource(R.string.dialog_text_delete_all_bookmarks)
    val deleteAllBookmarksMessage =
        stringResource(R.string.dialog_text_delete_all_desc)

    val positiveButtonText =
        stringResource(id = R.string.dialog_text_delete)
    val negativeButtonText =
        stringResource(id = R.string.dialog_text_cancel)

    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(
                            color = TabletBlockBg,
                            shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(horizontal = MaterialTheme.spacing.spaceTwelve)
                    .padding(top = MaterialTheme.spacing.spaceTwelve),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)
    ) {

        when {
            isSearchActive -> {
                SearchComponent(
                        modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.extraLarge)
                                .fillMaxWidth(),
                        searchTextFieldState = uiState.searchState.searchTextFieldState,
                        searchResultItems = uiState.searchState.searchResults,
                        expanded = uiState.searchState.searchTextFieldState.text.isNotBlank(),
                        showBackButton = false,
                        showSearchIconWhenEmpty = false,
                        isDeviceWide = true,
                        onSearch = { onEvent(BookmarksUiEvent.SearchClicked) },
                        onOpenBook = { onEvent(BookmarksUiEvent.OpenBook(bookId = it)) },
                        onClearSearchText = { onEvent(BookmarksUiEvent.ClearSearchClicked) },
                        onExitSearch = { onEvent(BookmarksUiEvent.ExitSearch) },
                )
            }

            else -> {
                WideDummySearchBar { onEvent(BookmarksUiEvent.SearchClicked) }
            }
        }

        uiState.globalBookmarkUiItems.ifEmpty {
            EmptyBookmarkScreen(isGlobalScreen = true, isDeviceWide = false)
        }

        Box(modifier = Modifier.fillMaxSize()) {
            LazyListComponent(
                    items = uiState.globalBookmarkUiItems,
                    key = { it.bookId },
                    isDeviceWide = isDeviceWide
            ) { item ->
                BookmarkCard(
                        item = item,
                        selected = uiState.selectedMenuItemId == item.bookId,
                        isMenuExpanded = uiState.selectedMenuItemId == item.bookId,
                        onItemClick = { onEvent(BookmarksUiEvent.OpenBook(item.bookId)) },
                        onViewBookmarks = { onEvent(BookmarksUiEvent.ViewBookmarks(item.bookId)) },
                        onDeleteBookmarks = {
                            onEvent(
                                    BookmarksUiEvent.DeleteBookmarks(
                                            bookId = item.bookId,
                                            dialogTitle = deleteAllBookmarksTitle,
                                            dialogMessage = deleteAllBookmarksMessage,
                                            positiveButtonText = positiveButtonText,
                                            negativeButtonText = negativeButtonText
                                    )
                            )
                        },
                        onOpenContextMenu = { onEvent(BookmarksUiEvent.ContextMenuClicked(item.bookId)) },
                        onDismissMenu = { onEvent(BookmarksUiEvent.DismissContextMenu) }
                )
            }

            if (uiState.isImporting) {
                CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                )
            }

            BookmarksDialog(
                    uiState = uiState,
                    onEvent = onEvent
            )
        }
    }
}

@Composable
private fun CompactBookmarksLayout(
    modifier: Modifier = Modifier,
    uiState: BookmarksUiState,
    onEvent: (BookmarksUiEvent) -> Unit
) {
    val deleteAllBookmarksTitle =
        stringResource(R.string.dialog_text_delete_all_bookmarks)

    val deleteAllBookmarksMessage =
        stringResource(R.string.dialog_text_delete_all_desc)

    val positiveButtonText =
        stringResource(id = R.string.dialog_text_delete)

    val negativeButtonText =
        stringResource(id = R.string.dialog_text_cancel)

    Box(modifier = modifier.fillMaxWidth()) {

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
                    onItemClick = { onEvent(BookmarksUiEvent.OpenBook(item.bookId)) },
                    onViewBookmarks = { onEvent(BookmarksUiEvent.ViewBookmarks(item.bookId)) },
                    onDeleteBookmarks = {
                        onEvent(
                                BookmarksUiEvent.DeleteBookmarks(
                                        bookId = item.bookId,
                                        dialogTitle = deleteAllBookmarksTitle,
                                        dialogMessage = deleteAllBookmarksMessage,
                                        positiveButtonText = positiveButtonText,
                                        negativeButtonText = negativeButtonText
                                )
                        )
                    },
                    onOpenContextMenu = { onEvent(BookmarksUiEvent.ContextMenuClicked(item.bookId)) },
                    onDismissMenu = { onEvent(BookmarksUiEvent.DismissContextMenu) }
            )
        }

        BookmarksDialog(
                uiState = uiState,
                onEvent = onEvent
        )

        if (uiState.isImporting) {
            CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = Primary
            )
        }
    }
}

@Composable
private fun BookmarksDialog(
    uiState: BookmarksUiState,
    onEvent: (BookmarksUiEvent) -> Unit
) {
    uiState.bookmarkDialogState?.let { dialogState ->
        AppDialog(
                dialogTitle = dialogState.title,
                dialogText = dialogState.message,
                positiveButtonText = dialogState.positiveButtonText,
                negativeButtonText = dialogState.negativeButtonText,
                isDeleteDialog = dialogState.type == BookmarksDialogType.DeleteBookmarks,
                onDismissRequest = { onEvent(BookmarksUiEvent.CancelDeleteDialog) },
                onConfirm = {
                    when (dialogState.type) {
                        BookmarksDialogType.DeleteBookmarks -> {
                            onEvent(BookmarksUiEvent.ConfirmDelete)
                            Timber.tag("BookmarksDialog").i("Confirming delete bookmarks")
                        }

                        BookmarksDialogType.UnsupportedFile -> {
                            onEvent(BookmarksUiEvent.CancelDeleteDialog)
                        }
                    }
                }
        )
    }
}



