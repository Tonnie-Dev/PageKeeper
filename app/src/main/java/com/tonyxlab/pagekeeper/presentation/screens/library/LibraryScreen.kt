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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppDialog
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyBooksScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyFavoritesScreen
import com.tonyxlab.pagekeeper.presentation.core.components.EmptyFinishedScreen
import com.tonyxlab.pagekeeper.presentation.core.components.SelectionTopBar
import com.tonyxlab.pagekeeper.presentation.screens.library.components.BookCard
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryNavigationDrawer
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryNavigationRail
import com.tonyxlab.pagekeeper.presentation.screens.library.components.SearchComponent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDialogType
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryDrawerDestination
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LibraryScreen(viewModel: LibraryViewModel = koinViewModel()) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val inSearchMode = uiState.searchState.isSearchMode
    val selectionState = uiState.selectionState
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isNavigationRailExpanded by rememberSaveable { mutableStateOf(false) }


    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        if (maxWidth >= 600.dp) {
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
                        }
                )

                Box(
                        modifier = Modifier
                                .weight(1f)
                                .fillMaxSize()
                ) {
                    BaseContent(
                            showNavigationIcon = false,
                            showImportFab = false,
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
                BaseContent(
                        showNavigationIcon = true,
                        showImportFab = true,
                        viewModel = viewModel,

                        onNavButtonClick = {
                            coroutineScope.launch { drawerState.open() }
                        }
                )
            }
        }
    }
}

@Composable
fun BaseContent(
    showNavigationIcon: Boolean,
    showImportFab: Boolean,
    onNavButtonClick: () -> Unit,
    viewModel: LibraryViewModel,

    ) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val inSearchMode = uiState.searchState.isSearchMode
    val selectionState = uiState.selectionState

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
            viewModel = viewModel,
            topBar = {
                if (inSearchMode.not()) {
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
                        AppTopBar(
                                titleText = stringResource(
                                        id = when (uiState.selectedDrawerDestination) {
                                            LibraryDrawerDestination.Library -> R.string.topbar_text_library
                                            LibraryDrawerDestination.Favorites -> R.string.topbar_text_favorites
                                            LibraryDrawerDestination.Finished -> R.string.topbar_text_finished
                                        }
                                ),
                                showNavigationIcon = showNavigationIcon,
                                onNavButtonClick = onNavButtonClick,
                                onActionClick = {
                                    viewModel.onEvent(LibraryUiEvent.SearchClicked)
                                }
                        )
                    }

                }
            },

            floatingActionButton = {
                if (showImportFab && inSearchMode.not() && uiState.books.isNotEmpty()) {
                    FloatingActionButton(
                            onClick = { viewModel.onEvent(LibraryUiEvent.ImportBookClicked) },
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
                        Toast.makeText(
                                actionContext,
                                "Reader is not available yet.",
                                Toast.LENGTH_SHORT
                        )
                                .show()
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

        LibraryScreenContent(
                modifier = Modifier,
                uiState = state,
                onEvent = viewModel::onEvent
        )
    }
}



@Composable
fun LibraryScreenContent(
    modifier: Modifier,
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit
) {
    val visibleBooks = when (uiState.selectedDrawerDestination) {
        LibraryDrawerDestination.Library -> uiState.books
        LibraryDrawerDestination.Favorites -> uiState.books.filter { it.isFavorite }
        LibraryDrawerDestination.Finished -> uiState.books.filter { it.isFinished }
    }

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
                        EmptyFavoritesScreen()
                    }

                    LibraryDrawerDestination.Finished -> {
                        EmptyFinishedScreen()
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


