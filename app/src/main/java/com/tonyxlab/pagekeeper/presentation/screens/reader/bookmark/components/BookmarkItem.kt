package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.core.components.AppDropdownMenu
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.BookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.toColor
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.Divider
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.ifThen
import com.tonyxlab.pagekeeper.utils.toDpSize
import com.tonyxlab.pagekeeper.utils.toFormattedDate

@Composable
fun BookmarkItem(
    bookmarkUiItem: BookmarkUiItem,
    modifier: Modifier = Modifier,
    selected: Boolean,
    onBookmarkClicked: (BookmarkUiItem) -> Unit,
    onEditClick: (BookmarkUiItem) -> Unit,
    onDeleteClick: (BookmarkUiItem) -> Unit,
    isMenuExpanded: Boolean,
    onOpenMenu: () -> Unit,
    onDismissMenu: () -> Unit,
) {
    Column(
            modifier = modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()

    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                    modifier = Modifier
                            .weight(1f)
                            .clickable { onBookmarkClicked(bookmarkUiItem) }
                            .padding(
                                    start = MaterialTheme.spacing.spaceTwelve * 2,
                                    top = MaterialTheme.spacing.spaceTwelve,
                                    end = MaterialTheme.spacing.spaceTwelve,
                                    bottom = MaterialTheme.spacing.spaceSmall,
                            ),
                    verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                        modifier = Modifier.size(MaterialTheme.spacing.spaceTwelve * 2),
                        painter = painterResource(R.drawable.ic_bookmark_filled),
                        contentDescription = null,
                        tint = bookmarkUiItem.color.toColor(),
                )

                Spacer(Modifier.width(MaterialTheme.spacing.spaceTwelve * 2))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                            text = bookmarkUiItem.text,
                            style = MaterialTheme.typography.BodySmallRegular,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                    )

                    Row(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = MaterialTheme.spacing.spaceSmall),
                            verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                                modifier = Modifier.weight(1f),
                                text = bookmarkUiItem.chapterTitle,
                                style = MaterialTheme.typography.BodySmallRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                        )

                        Spacer(Modifier.width(MaterialTheme.spacing.spaceTwelve))

                        Text(
                                text = bookmarkUiItem.createdAt.toFormattedDate(),
                                style = MaterialTheme.typography.BodySmallRegular,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                        )
                    }
                }
            }

            Box(modifier = Modifier.padding(all = MaterialTheme.spacing.spaceSmall)) {

                IconButton(
                        modifier = Modifier
                                .size(MENU_BUBBLE_SIZE.toDpSize())
                                .ifThen(selected) {
                                    background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.large
                                    )
                                },
                        onClick = {
                            onOpenMenu()
                        }
                ) {

                    Icon(
                            painter = painterResource(R.drawable.ic_context_menu),
                            contentDescription = stringResource(R.string.cds_text_menu),
                            tint = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.onSurface
                            }
                    )
                }

                AppDropdownMenu(
                        item = bookmarkUiItem,
                        isMenuExpanded = isMenuExpanded,
                        modifier = Modifier,
                        isSmallMenu = true,
                        onDismissMenu = onDismissMenu,
                        onEditClick = onEditClick,
                        onDeleteClick = onDeleteClick
                )
            }
        }
        HorizontalDivider(color = Divider)
    }
}

private val MENU_BUBBLE_SIZE = IntSize(48, 48)

@Preview(showBackground = true)
@Composable
private fun BookmarkItem_Preview() {
    PageKeeperTheme {
        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {
            val bookmark = BookmarkUiItem(
                    id = 879,
                    blockIndex = 5690,
                    text = "Forest was immensely Dark, so dark that I could not see past my nose",
                    chapterTitle = "Chapter 1 of the Very Dark Forest",
                    color = BookmarkColor.Yellow,
                    createdAt = 7855,
                    textOffset = 0
            )
            BookmarkItem(
                    bookmarkUiItem = bookmark,
                    selected = true,
                    isMenuExpanded = false,
                    onOpenMenu = {},
                    onDismissMenu = {},
                    onBookmarkClicked = {},
                    onEditClick = {},
                    onDeleteClick = {}
            )
        }
    }
}

