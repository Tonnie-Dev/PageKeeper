package com.tonyxlab.pagekeeper.presentation.screens.library

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppDialog
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBooksScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyFavoritesScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyFinishedScreen
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.library.components.BookCard
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryNavigationDrawer
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryNavigationRail
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryTopBar
import com.tonyxlab.pagekeeper.presentation.screens.library.components.SearchComponent
import com.tonyxlab.pagekeeper.presentation.screens.library.components.SelectionTopBar
import com.tonyxlab.pagekeeper.presentation.screens.library.components.WideDummySearchBar
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialogType
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDrawerDestination
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
    viewModel: LibraryViewModel = koinViewModel(),
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val inSearchMode = uiState.searchState.isSearchMode
    val selectionState = uiState.selectionState

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isNavigationRailExpanded by rememberSaveable { mutableStateOf(false) }

    val isDeviceWide = rememberIsDeviceWide()

    if (isDeviceWide) {
        Row(modifier = Modifier.fillMaxSize()) {
            LibraryNavigationRail(
                    selectedDestination = uiState.selectedDrawerDestination,
                    expanded = isNavigationRailExpanded,
                    onExpandedChange = { isNavigationRailExpanded = it },
                    onImportBookClick = {
                        viewModel.onEvent(LibraryUiEvent.ImportBookClicked)
                    },
                    onDestinationClick = { destination ->
                        viewModel.onEvent(LibraryUiEvent.DrawerDestinationClicked(destination))
                    },
                    exitSearch = {
                        viewModel.onEvent(LibraryUiEvent.SearchBackClicked)
                    }
            )

            Box(
                    modifier = Modifier
                            .weight(1f)
                            .fillMaxSize()
            ) {
                MergedLibraryLayout(
                        showNavigationIcon = false,
                        showImportFab = false,
                        showTopBar = false,
                        modifier = Modifier.padding(
                                top = MaterialTheme.spacing.spaceLarge,
                                start = MaterialTheme.spacing.spaceMedium,
                                end = MaterialTheme.spacing.spaceMedium,
                                bottom = MaterialTheme.spacing.spaceMedium
                        ),
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
                    LibraryNavigationDrawer(
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
                    showImportFab = true,
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
    showImportFab: Boolean,
    showTopBar: Boolean,
    onNavButtonClick: () -> Unit,
    navigator: Navigator,
    viewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
) {

    val isDeviceWide = rememberIsDeviceWide()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val inSearchMode = uiState.searchState.isSearchMode
    val selectionState = uiState.selectionState

    var isNavigatingAway by remember {
        mutableStateOf(false)
    }

    val filePicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri ?: return@rememberLauncherForActivityResult
        viewModel.onEvent(
                LibraryUiEvent.FileSelected(
                        uri = uri,
                        fileName = context.getDisplayName(uri)
                )
        )
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
                                            LibraryDrawerDestination.Library -> R.string.topbar_text_library
                                            LibraryDrawerDestination.Favorites -> R.string.topbar_text_favorites
                                            LibraryDrawerDestination.Finished -> R.string.topbar_text_finished
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
                    !isNavigatingAway
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
                    LibraryActionEvent.OpenFilePicker -> filePicker.launch("*/*")
                    is LibraryActionEvent.OpenBook -> {
                        isNavigatingAway = true
                        navigator.navigateToRead(actionEvent.bookId)
                    }

                    is LibraryActionEvent.ShareBook -> actionContext.shareBook(
                            actionEvent.bookId,
                            viewModel.uiState.value
                    )

                    is LibraryActionEvent.ShowToast -> {
                        Toast.makeText(actionContext, actionEvent.message, Toast.LENGTH_SHORT)
                                .show()
                    }
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

    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(
                            color = TabletBlockBg,
                            shape = MaterialTheme.shapes.extraLarge
                    )
                    .padding(start = MaterialTheme.spacing.spaceMedium)
                    .padding(top = MaterialTheme.spacing.spaceTwelve),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTwelve)
    ) {

        val isSearchActive = uiState.searchState.isSearchMode

        if (isSearchActive) {
            SearchComponent(
                    modifier = Modifier
                            .clip(shape = MaterialTheme.shapes.extraLarge)
                            .fillMaxWidth(),
                    uiState = uiState,
                    onEvent = onEvent,
                    expanded = uiState.searchState.isSearchMode,
                    showBackButton = false,
                    showSearchIconWhenEmpty = uiState.searchState.isSearchMode.not(),
                    inputHeight = 40.dp,
                    isDeviceWide = true
            )
        } else {
            WideDummySearchBar { onEvent(LibraryUiEvent.SearchClicked) }
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
                        LibraryDrawerDestination.Favorites -> {
                            EmptyFavoritesScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    isDeviceWide = true
                            )
                        }

                        LibraryDrawerDestination.Finished -> {
                            EmptyFinishedScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    isDeviceWide = true
                            )
                        }

                        LibraryDrawerDestination.Library -> {
                            EmptyBooksScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    backgroundColor = TabletBlockBg,
                                    imageSize = 114.dp,
                                    iconSize = 64.dp,
                                    onImportBookClick = { onEvent(LibraryUiEvent.ImportBookClicked) }
                            )
                        }
                    }
                }

                visibleBooks.isNotEmpty() && uiState.searchState.isSearchMode.not() -> {
                    LazyVerticalGrid(
                            modifier = Modifier.fillMaxSize(),
                            columns = GridCells.Adaptive(minSize = 280.dp),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
                    ) {
                        items(items = visibleBooks, key = { it.id }) { book ->
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
                        uiState = uiState,
                        onEvent = onEvent
                )
            }

            visibleBooks.isEmpty() -> {
                when (uiState.selectedDrawerDestination) {

                    LibraryDrawerDestination.Favorites -> {
                        EmptyFavoritesScreen(isDeviceWide = false)
                    }

                    LibraryDrawerDestination.Finished -> {
                        EmptyFinishedScreen(isDeviceWide = false)
                    }

                    LibraryDrawerDestination.Library -> {
                        EmptyBooksScreen(
                                onImportBookClick = { onEvent(LibraryUiEvent.ImportBookClicked) }
                        )
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
    LibraryDrawerDestination.Library -> books
    LibraryDrawerDestination.Favorites -> books.filter { it.isFavorite }
    LibraryDrawerDestination.Finished -> books.filter { it.isFinished }
}

private fun Context.getDisplayName(uri: Uri): String {
    val cursor: Cursor? = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
    )
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && it.moveToFirst()) {
            return it.getString(nameIndex)
                    .orEmpty()
        }
    }
    return uri.lastPathSegment.orEmpty()
}

private fun Context.shareBook(bookId: String, uiState: LibraryUiState) {
    val book = uiState.books.firstOrNull { it.id == bookId }
    if (book == null) {
        Toast.makeText(this, "Unable to share book.", Toast.LENGTH_SHORT)
                .show()
        return
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, book.title)
        putExtra(Intent.EXTRA_TEXT, "${book.title} by ${book.author}")
    }
    startActivity(Intent.createChooser(intent, null))
}


