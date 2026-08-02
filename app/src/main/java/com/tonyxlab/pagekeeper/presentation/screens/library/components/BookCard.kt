package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.presentation.core.components.AppButton
import com.tonyxlab.pagekeeper.presentation.core.components.ReadProgressBar
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumRegular
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.TitleLargeBold
import com.tonyxlab.pagekeeper.presentation.theme.TitleSmallMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.ifThen

@Composable
fun BookCard(
    book: Book,
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    isDeviceWide: Boolean = false,
    isResumeBook: Boolean = false
) {
    val selectionState = uiState.selectionState
    val isSelectionMode = uiState.selectionState.isSelectionMode
    val isSelected = selectionState.selectedBooksIds.isSelected(book.id)

    val containerColor = when {
        isSelected ->
            MaterialTheme.colorScheme.secondary

        isResumeBook ->
            MaterialTheme.colorScheme.background

        isDeviceWide && isSelectionMode ->
            MaterialTheme.colorScheme.background

        isDeviceWide ->
            Color.Transparent

        else ->
            MaterialTheme.colorScheme.background
    }

    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(
                            vertical = if (isSelectionMode)
                                MaterialTheme.spacing.spaceExtraSmall
                            else
                                MaterialTheme.spacing.spaceDefault
                    )
                    .combinedClickable(
                            onClick = {
                                if (selectionState.isSelectionMode) {
                                    onEvent(LibraryUiEvent.BookSelectionToggled(book.id))
                                } else {
                                    onEvent(LibraryUiEvent.OpenBook(book.id))
                                }
                            },
                            onLongClick = { onEvent(LibraryUiEvent.BookLongClicked(book.id)) }
                    ),
            shape = MaterialTheme.shapes.small,
            color = containerColor,
            border = if (isSelectionMode) {
                BorderStroke(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline
                )

            } else null

    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .ifThen(isSelectionMode) {
                            padding(vertical = MaterialTheme.spacing.spaceTwelve)
                                    .padding(horizontal = MaterialTheme.spacing.spaceTwelve)
                        }
                        .ifThen(isSelectionMode.not()) {
                            padding(MaterialTheme.spacing.spaceTwelve)
                        },
                verticalAlignment = Alignment.CenterVertically
        ) {
            if (selectionState.isSelectionMode) {
                Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            onEvent(LibraryUiEvent.BookSelectionToggled(book.id))
                        },
                        colors = CheckboxDefaults.colors(
                                checkedColor = Primary,
                                uncheckedColor = IconsTint,
                                checkmarkColor = MaterialTheme.colorScheme.onPrimary
                        )
                )
            }

            book.coverPath?.let { cover ->
                AsyncImage(
                        modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = MaterialTheme.shapes.small
                                )
                                .size(
                                        width = isResumeBook.coverWidth(),
                                        height = isResumeBook.coverHeight()
                                ),
                        model = cover,
                        contentDescription = book.title,
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                )
            } ?: BookCoverPlaceholder(isResumeBook = isResumeBook)

            Column(
                    modifier = Modifier
                            .weight(1f)
                            .padding(start = MaterialTheme.spacing.spaceTwelve)
                            .fillMaxHeight()
            ) {
                Row {
                    Text(
                            modifier = Modifier.weight(1f),
                            text = book.title,
                            style = if (isResumeBook)
                                MaterialTheme.typography.TitleLargeBold
                            else
                                MaterialTheme.typography.TitleSmallMedium,
                            color = TextPrimary,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis
                    )

                    if (isDeviceWide && isResumeBook) {

                        AppButton(
                                buttonText = "Continue",
                                leadingIcon = painterResource(id = R.drawable.ic_import_book),
                                onClick = { onEvent(LibraryUiEvent.ResumeBook) }
                        )
                    }
                }

                Text(
                        modifier = Modifier.padding(top = MaterialTheme.spacing.spaceExtraSmall),
                        text = book.author,
                        style = if (isResumeBook)
                            MaterialTheme.typography.BodyMediumRegular
                        else
                            MaterialTheme.typography.BodySmallRegular,
                        color = TextSecondary,
                        maxLines = 2
                )

                Spacer(modifier = Modifier.weight(1f))

                ReadProgressBar(
                        modifier = Modifier.padding(vertical = MaterialTheme.spacing.spaceDoubleDp),
                        progress = book.progress
                )

                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
                ) {
                    Row(

                            horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(onClick = { onEvent(LibraryUiEvent.MarkFavorite(book.id)) }) {
                            Icon(
                                    painter = if (book.isFavorite) {
                                        painterResource(id = R.drawable.ic_star_filled)
                                    } else {
                                        painterResource(id = R.drawable.ic_star_outlined)
                                    },
                                    contentDescription = stringResource(id = R.string.cds_text_favorite),
                                    tint = if (book.isFavorite) Primary else IconsTint,
                            )
                        }

                        IconButton(onClick = { onEvent(LibraryUiEvent.FinishBook(book.id)) }) {
                            Icon(
                                    painter = if (book.isFinished) {
                                        painterResource(id = R.drawable.ic_mark_finished)
                                    } else {
                                        painterResource(id = R.drawable.ic_finished_outlined)
                                    },
                                    contentDescription = stringResource(id = R.string.cds_text_bookmark),
                                    tint = IconsTint
                            )
                        }

                        IconButton(onClick = { onEvent(LibraryUiEvent.ShareBook(bookId = book.id)) }) {
                            Icon(
                                    painter = painterResource(id = R.drawable.ic_share),
                                    contentDescription = stringResource(id = R.string.cds_text_share),
                                    tint = IconsTint,
                            )
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onEvent(LibraryUiEvent.ConfirmDeleteDialog(bookId = book.id)) }) {
                        Icon(
                                painter = painterResource(id = R.drawable.ic_delete),
                                contentDescription = stringResource(id = R.string.cds_text_delete),
                                tint = IconsTint,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BookCoverPlaceholder(
    isResumeBook: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                    )
                    .size(
                            width = isResumeBook.coverWidth(),
                            height = isResumeBook.coverHeight()
                    ),
            contentAlignment = Alignment.Center
    ) {
        Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = Color(0xFF7A7771),
                modifier = Modifier.size(56.dp)
        )
    }
}

private fun Set<String>.isSelected(id: String): Boolean = id in this

@Preview(showBackground = true, backgroundColor = 0xFFFDFCF8)
@Composable
fun BookCardPreview() {
    PageKeeperTheme {

        val book = Book(
                id = "1",
                title = "The Fellowship of the Ring (Book 1) (Illustrated Edition)",
                author = "J.R.R. Tolkien",
                coverPath = null,
                fileName = "fellowship.epub",
                filePath = "/books/fellowship.epub",
                dateAdded = System.currentTimeMillis()
        )
        Column(modifier = Modifier.fillMaxSize()) {

            BookCard(
                    book = book,
                    uiState = LibraryUiState(
                            selectionState = LibraryUiState.SelectionState(
                                    isSelectionMode = false,
                                    selectedBooksIds = emptySet()
                            )
                    ),
                    onEvent = {}
            )

            BookCard(
                    book = book,
                    uiState = LibraryUiState(
                            selectionState = LibraryUiState.SelectionState(
                                    isSelectionMode = true,
                                    selectedBooksIds = emptySet()
                            )
                    ),
                    onEvent = {}
            )

            BookCard(
                    book = book,
                    uiState = LibraryUiState(
                            selectionState = LibraryUiState.SelectionState(
                                    isSelectionMode = true,
                                    selectedBooksIds = setOf("1")
                            )
                    ),
                    onEvent = {}
            )

        }
    }
}

private fun Boolean.coverWidth() =
    if (this) 160.dp else 104.dp

private fun Boolean.coverHeight() =
    if (this) 240.dp else 156.dp
