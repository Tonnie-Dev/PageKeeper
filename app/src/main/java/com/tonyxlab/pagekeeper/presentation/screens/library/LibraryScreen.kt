package com.tonyxlab.pagekeeper.presentation.screens.library

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppDialog
import com.tonyxlab.pagekeeper.presentation.core.components.AppNavigationDrawer
import com.tonyxlab.pagekeeper.presentation.core.components.AppNavigationRail
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBookmarkScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBooksScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyFavoritesScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyFinishedScreen
import com.tonyxlab.pagekeeper.presentation.core.components.SearchComponent
import com.tonyxlab.pagekeeper.presentation.core.components.WideDummySearchBar
import com.tonyxlab.pagekeeper.presentation.core.utils.rememberFilePicker
import com.tonyxlab.pagekeeper.presentation.core.utils.shareBook
import com.tonyxlab.pagekeeper.presentation.navigation.AppNavigationDestination
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.library.components.BookCard
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryTopBar
import com.tonyxlab.pagekeeper.presentation.screens.library.components.SelectionTopBar
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialogType
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.rememberIsDeviceWide
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibraryScreen(
    navigator: Navigator,
    initialDestination: AppNavigationDestination = AppNavigationDestination.Library,
    viewModel: LibraryViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val inSearchMode = uiState.searchState.isSearchMode
    val selectionState = uiState.selectionState

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isNavigationRailExpanded by rememberSaveable { mutableStateOf(false) }

    val isDeviceWide = rememberIsDeviceWide()

    LaunchedEffect(initialDestination) {

        if (initialDestination != AppNavigationDestination.Bookmarks) {
            viewModel.onEvent(LibraryUiEvent.DrawerDestinationClicked(destination = initialDestination))
        }
    }

    if (isDeviceWide) {
        Row(modifier = Modifier.fillMaxSize()) {
            AppNavigationRail(
                    selectedDestination = uiState.selectedDrawerDestination,
                    expanded = isNavigationRailExpanded,
                    onExpandedChange = { isNavigationRailExpanded = it },
                    onImportBookClick = { viewModel.onEvent(LibraryUiEvent.ImportBookClicked) },
                    onDestinationClick = { destination ->
                        viewModel.onEvent(LibraryUiEvent.DrawerDestinationClicked(destination))
                    },
                    exitSearch = { viewModel.onEvent(LibraryUiEvent.ExitSearch) }
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
                        viewModel = viewModel,
                )
            }
        }
    } else {
        ModalNavigationDrawer(
                drawerState = drawerState,
                gesturesEnabled = inSearchMode.not() && selectionState.isSelectionMode.not(),
                scrimColor = Color.Black.copy(alpha = 0.38f),
                drawerContent = {
                    AppNavigationDrawer(
                            selectedDestination = uiState.selectedDrawerDestination,
                            onCloseClick = {
                                coroutineScope.launch { drawerState.close() }
                            },
                            onImportBookClick = {
                                coroutineScope.launch { drawerState.close() }
                                viewModel.onEvent(LibraryUiEvent.ImportBookClicked)
                            },
                            onDestinationClick = { destination ->
                                viewModel.onEvent(
                                        LibraryUiEvent.DrawerDestinationClicked(
                                                destination
                                        )
                                )
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
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {

    val isDeviceWide = rememberIsDeviceWide()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val inSearchMode = uiState.searchState.isSearchMode
    val selectionState = uiState.selectionState

    var isNavigatingAway by remember {
        mutableStateOf(false)
    }

    val filePicker = rememberFilePicker { uri, fileName ->

        viewModel.onEvent(LibraryUiEvent.FileSelected(uri, fileName))

    }
    BaseContentLayout(
            modifier = modifier,
            viewModel = viewModel,
            topBar = {
                if (showTopBar && inSearchMode.not()) {
                    if (selectionState.isSelectionMode) {
                        SelectionTopBar(
                                selectedCount = selectionState.selectedCount,
                                onBackClick = {
                                    viewModel.onEvent(LibraryUiEvent.ExitSelectionModeClicked)
                                },
                                onFavoriteClick = {
                                    viewModel.onEvent(LibraryUiEvent.AddSelectedToFavoritesClicked)
                                },
                                onShareClick = {
                                    viewModel.onEvent(LibraryUiEvent.ShareSelectedClicked)
                                },
                                onDeleteClick = {
                                    viewModel.onEvent(LibraryUiEvent.DeleteSelectedClicked)
                                }
                        )
                    } else {
                        LibraryTopBar(
                                titleText = stringResource(
                                        id = when (uiState.selectedDrawerDestination) {
                                            AppNavigationDestination.Library -> R.string.topbar_text_library
                                            AppNavigationDestination.Favorites -> R.string.topbar_text_favorites
                                            AppNavigationDestination.Finished -> R.string.topbar_text_finished
                                            AppNavigationDestination.Bookmarks -> R.string.topbar_text_bookmarks
                                        }
                                ),
                                showNavIcon = showNavigationIcon,
                                onNavButtonClick = onNavButtonClick,
                                onActionClick = {
                                    viewModel.onEvent(LibraryUiEvent.SearchClicked)
                                }
                        )
                    }
                }
            },
            floatingActionButton = {
                if (uiState.resumeBook != null &&
                    !inSearchMode &&
                    !isNavigatingAway &&
                    !isDeviceWide
                ) {
                    FloatingActionButton(
                            modifier = Modifier.navigationBarsPadding(),
                            onClick = { viewModel.onEvent(LibraryUiEvent.ResumeBook) },
                            containerColor = Primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                    ) {
                        Icon(
                                painter = painterResource(id = R.drawable.ic_import_book),
                                contentDescription = stringResource(id = R.string.button_text_import_book)
                        )
                    }
                }
            },
            actionEventHandler = { actionContext, actionEvent ->
                when (actionEvent) {
                    LibraryActionEvent.OpenFilePicker -> {
                        filePicker.launch("*/*")
                    }

                    is LibraryActionEvent.OpenBook -> {
                        isNavigatingAway = true
                        navigator.navigateToRead(actionEvent.bookId)
                    }

                    is LibraryActionEvent.ShareBook -> {
                        actionContext.shareBook(
                                actionEvent.bookId,
                                viewModel.uiState.value
                        )
                    }

                    is LibraryActionEvent.ShowToast -> {
                        Toast.makeText(actionContext, actionEvent.message, Toast.LENGTH_SHORT)
                                .show()
                    }

                    LibraryActionEvent.NavigateToBookmarks -> navigator.navigateToGlobalBookmarks()
                }
            }
    ) { state ->

        if (isDeviceWide) {
            WideLibraryLayout(
                    modifier = Modifier,
                    uiState = state,
                    onEvent = viewModel::onEvent
            )
        } else {
            CompactLibraryLayout(
                    modifier = Modifier,
                    uiState = state,
                    onEvent = viewModel::onEvent
            )
        }
    }
}

@Composable
private fun WideLibraryLayout(
    modifier: Modifier,
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit
) {

    val visibleBooks = uiState.visibleBooks()

    val regularBooks = remember(visibleBooks, uiState.resumeBook) {
        visibleBooks.filter { it.id != uiState.resumeBook?.id }
    }

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

        val isSearchActive = uiState.searchState.isSearchMode

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
                        onSearch = { onEvent(LibraryUiEvent.SearchClicked) },
                        onOpenBook = { onEvent(LibraryUiEvent.OpenBook(bookId = it)) },
                        onClearSearchText = { onEvent(LibraryUiEvent.ClearSearchClicked) },
                        onExitSearch = { onEvent(LibraryUiEvent.ExitSearch) },
                )
            }

            uiState.selectionState.isSelectionMode -> {
                SelectionTopBar(
                        selectedCount = uiState.selectionState.selectedCount,
                        backgroundColor = TabletBlockBg,
                        onBackClick = {
                            onEvent(LibraryUiEvent.ExitSelectionModeClicked)
                        },
                        onFavoriteClick = {
                            onEvent(LibraryUiEvent.AddSelectedToFavoritesClicked)
                        },
                        onShareClick = {
                            onEvent(LibraryUiEvent.ShareSelectedClicked)
                        },
                        onDeleteClick = {
                            onEvent(LibraryUiEvent.DeleteSelectedClicked)
                        }
                )
            }

            else -> {
                WideDummySearchBar { onEvent(LibraryUiEvent.SearchClicked) }
            }
        }

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading || uiState.isImporting -> {
                    CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = Primary
                    )
                }

                visibleBooks.isEmpty() && uiState.searchState.isSearchMode.not() -> {
                    when (uiState.selectedDrawerDestination) {
                        AppNavigationDestination.Favorites -> {
                            EmptyFavoritesScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    isDeviceWide = true
                            )
                        }

                        AppNavigationDestination.Finished -> {
                            EmptyFinishedScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    isDeviceWide = true
                            )
                        }

                        AppNavigationDestination.Library -> {
                            EmptyBooksScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    imageSize = 114.dp,
                                    iconSize = 64.dp,
                                    onImportBookClick = { onEvent(LibraryUiEvent.ImportBookClicked) }
                            )
                        }

                        AppNavigationDestination.Bookmarks -> {
                            EmptyBookmarkScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    isDeviceWide = true
                            )

                        }
                    }
                }

                visibleBooks.isNotEmpty() && uiState.searchState.isSearchMode.not() -> {
                    LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Fixed(count = 2),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
                    ) {

                        uiState.resumeBook?.let { resumeBook ->
                            item(
                                    key = "resume_book_${resumeBook.id}",
                                    span = { GridItemSpan(maxLineSpan) }
                            ) {

                                Column(modifier = Modifier.padding(all = MaterialTheme.spacing.spaceSmall)) {
                                    BookCard(
                                            modifier = Modifier,
                                            book = resumeBook,
                                            uiState = uiState,
                                            onEvent = onEvent,
                                            isDeviceWide = true,
                                            isResumeBook = true

                                    )
                                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
                                    HorizontalDivider(
                                            thickness = MaterialTheme.spacing.spaceDoubleDp,
                                            color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }

                        }
                        items(
                                items = regularBooks,
                                key = { it.id }) { book ->
                            BookCard(
                                    modifier = Modifier,
                                    book = book,
                                    uiState = uiState,
                                    onEvent = onEvent,
                                    isDeviceWide = true
                            )
                        }
                    }
                }
            }
            LibraryDialog(
                    uiState = uiState,
                    onEvent = onEvent
            )
        }
    }
}

@Composable
private fun CompactLibraryLayout(
    modifier: Modifier,
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit
) {
    val visibleBooks = uiState.visibleBooks()

    Box(
            modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = MaterialTheme.spacing.spaceMedium)
    ) {
        when {
            uiState.isLoading || uiState.isImporting -> {
                CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Primary
                )
            }

            uiState.searchState.isSearchMode -> {

                SearchComponent(
                        modifier = Modifier,
                        searchTextFieldState = uiState.searchState.searchTextFieldState,
                        searchResultItems = uiState.searchState.searchResults,
                        expanded = uiState.searchState.searchTextFieldState.text.isNotBlank(),
                        showBackButton = true,
                        showSearchIconWhenEmpty = true,
                        isDeviceWide = false,
                        onSearch = { onEvent(LibraryUiEvent.SearchClicked) },
                        onOpenBook = { onEvent(LibraryUiEvent.OpenBook(bookId = it)) },
                        onClearSearchText = { onEvent(LibraryUiEvent.ClearSearchClicked) },
                        onExitSearch = { onEvent(LibraryUiEvent.ExitSearch) },
                )
            }

            visibleBooks.isEmpty() -> {
                when (uiState.selectedDrawerDestination) {

                    AppNavigationDestination.Favorites -> {
                        EmptyFavoritesScreen(isDeviceWide = false)
                    }

                    AppNavigationDestination.Finished -> {
                        EmptyFinishedScreen(isDeviceWide = false)
                    }

                    AppNavigationDestination.Library -> {
                        EmptyBooksScreen(
                                onImportBookClick = { onEvent(LibraryUiEvent.ImportBookClicked) }
                        )
                    }

                    AppNavigationDestination.Bookmarks -> {
                        EmptyBookmarkScreen(isDeviceWide = false)
                    }
                }
            }

            else -> {
                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.spaceSmall)

                ) {
                    items(items = visibleBooks, key = { it.id }) { book ->

                        BookCard(
                                modifier = Modifier,
                                book = book,
                                uiState = uiState,
                                onEvent = onEvent
                        )
                    }
                }
            }
        }
        LibraryDialog(uiState = uiState, onEvent = onEvent)
    }
}

@Composable
private fun LibraryDialog(
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit
) {
    uiState.dialog?.let { dialog ->
        AppDialog(
                dialogTitle = dialog.title,
                dialogText = dialog.message,
                positiveButtonText = dialog.positiveButtonText,
                negativeButtonText = dialog.negativeButtonText,
                isDeleteDialog = dialog.type == LibraryDialogType.DeleteBook ||
                        dialog.type == LibraryDialogType.DeleteSelectedBooks,
                onDismissRequest = { onEvent(LibraryUiEvent.DismissDialog) },
                onConfirm = {
                    when (dialog.type) {
                        LibraryDialogType.DeleteBook -> {
                            dialog.bookId?.let { onEvent(LibraryUiEvent.DeleteBook(it)) }
                        }

                        LibraryDialogType.DeleteSelectedBooks -> {
                            onEvent(LibraryUiEvent.ConfirmDeleteSelectedClicked)
                        }

                        LibraryDialogType.UnsupportedFile -> onEvent(LibraryUiEvent.DismissDialog)
                    }
                }
        )
    }
}

private fun LibraryUiState.visibleBooks() = when (selectedDrawerDestination) {

    AppNavigationDestination.Favorites -> books.filter { it.isFavorite }
    AppNavigationDestination.Finished -> books.filter { it.isFinished }
    else -> books
}


