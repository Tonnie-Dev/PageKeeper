package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.Icons
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.TitleSmallMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    onFavoriteClick: (Book) -> Unit = {},
    onBookmarkClick: (Book) -> Unit = {},
    onShareClick: (Book) -> Unit = {},
    onDeleteClick: (Book) -> Unit = {},
) {
    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(MaterialTheme.spacing.spaceTwelve)
    ) {

        book.coverPath?.let { cover ->
            AsyncImage(
                    model = cover,
                    contentDescription = book.title,
                    modifier = Modifier
                            .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = MaterialTheme.shapes.small
                            )
                            .size(width = 104.dp, height = 156.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
            )
        } ?: BookCoverPlaceholder()

        Column(
                modifier = Modifier
                        .weight(1f)
                        .padding(start = MaterialTheme.spacing.spaceTwelve)
                        .fillMaxHeight()
        ) {
            Text(
                    text = book.title,
                    style = MaterialTheme.typography.TitleSmallMedium,
                    color = TextPrimary,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
            )

            Text(
                    text = book.author,
                    style = MaterialTheme.typography.BodySmallRegular,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.spaceExtraSmall)
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall)
            ) {

                IconButton(onClick = { onFavoriteClick(book) }) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_favorite),
                            contentDescription = stringResource(id = R.string.cds_text_favorite),
                            tint = if (book.isFavorite) Primary else Icons,
                    )
                }

                IconButton(onClick = { onBookmarkClick(book) }) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark),
                            contentDescription = stringResource(id = R.string.cds_text_bookmark),
                            tint = Icons
                    )
                }

                IconButton(onClick = { onShareClick(book) }) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = stringResource(id = R.string.cds_text_share),
                            tint = Icons,

                            )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { onDeleteClick(book) }) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(id = R.string.cds_text_delete),
                            tint = Icons,
                    )
                }
            }
        }
    }
}

@Composable
private fun BookCoverPlaceholder(modifier: Modifier = Modifier) {
    Box(
            modifier = modifier
                    .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                    )
                    .size(
                            width = 104.dp,
                            height = 156.dp
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

@Preview(showBackground = true, backgroundColor = 0xFFFDFCF8)
@Composable
fun BookCardPreview() {
    PageKeeperTheme {
        BookCard(
                book = Book(
                        id = "1",
                        title = "The Fellowship of the Ring (Book 1) (Illustrated Edition)",
                        author = "J.R.R. Tolkien",
                        coverPath = null,
                        fileName = "fellowship.epub",
                        filePath = "/books/fellowship.epub",
                        dateAdded = System.currentTimeMillis()
                )
        )
    }
}
