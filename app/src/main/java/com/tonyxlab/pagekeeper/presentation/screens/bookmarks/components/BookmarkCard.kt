package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.components.AppDropdownMenu
import com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model.GlobalBookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.theme.BodyLargeRegular
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.TitleSmallMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.ifThen
import com.tonyxlab.pagekeeper.utils.toDpSize

@Composable
fun BookmarkCard(
    item: GlobalBookmarkUiItem,
    selected: Boolean,
    isMenuExpanded: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: () -> Unit,
    onViewBookmarks: () -> Unit,
    onDeleteBookmarks: () -> Unit,
    onOpenContextMenu: () -> Unit,
    onDismissMenu: () -> Unit,
) {
    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .clickable { onItemClick() },
            shape = MaterialTheme.shapes.small,
            color = Color.Transparent,
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(horizontal = MaterialTheme.spacing.spaceTwelve)
                        .padding(vertical = MaterialTheme.spacing.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
        ) {

            item.coverPath?.let { cover ->
                AsyncImage(
                        modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = MaterialTheme.shapes.small
                                )
                                .size(
                                        width = COVER_WIDTH, height = COVER_HEIGHT
                                ),
                        model = cover,
                        contentDescription = item.title,
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

                Row {
                    Column(Modifier.weight(1f)) {
                        Text(
                                modifier = Modifier,
                                text = item.title,
                                style = MaterialTheme.typography.TitleSmallMedium,
                                color = TextPrimary,
                                maxLines = 3,
                                overflow = TextOverflow.Ellipsis
                        )

                        Text(
                                modifier = Modifier.padding(top = MaterialTheme.spacing.spaceExtraSmall),
                                text = item.author,
                                style = MaterialTheme.typography.BodySmallRegular,
                                color = TextSecondary,
                                maxLines = 2
                        )
                    }

                    Box(
                            modifier = Modifier.padding(all = MaterialTheme.spacing.spaceSmall)
                    ) {

                        IconButton(
                                modifier = Modifier
                                        .size(MENU_BUBBLE_SIZE.toDpSize())
                                        .ifThen(selected) {
                                            background(
                                                    color = MaterialTheme.colorScheme.primary,
                                                    shape = MaterialTheme.shapes.large
                                            )
                                        }, onClick = {
                            onOpenContextMenu()
                        }) {

                            Icon(
                                    painter = painterResource(R.drawable.ic_context_menu),
                                    contentDescription = stringResource(R.string.cds_text_menu),
                                    tint = if (selected) {
                                        MaterialTheme.colorScheme.onPrimary
                                    } else {
                                        IconsTint
                                    }
                            )
                        }

                        AppDropdownMenu(
                                modifier = Modifier,
                                actionOneText = stringResource(id = R.string.menu_text_view_bookmarks),
                                actionTwoText = stringResource(id = R.string.menu_text_delete_bookmarks),
                                isMenuExpanded = isMenuExpanded,
                                onDismissMenu = onDismissMenu,
                                onFirstAction = onViewBookmarks,
                                onSecondAction = onDeleteBookmarks
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                        modifier = Modifier.padding(bottom = MaterialTheme.spacing.spaceSmall),
                        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceExtraSmall),
                        verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                            painter = painterResource(id = R.drawable.ic_bookmark_big),
                            contentDescription = stringResource(id = R.string.cds_text_bookmark),
                            tint = IconsTint
                    )

                    Text(
                            modifier = Modifier,
                            text = item.bookmarkCount.toString(),
                            style = MaterialTheme.typography.BodyLargeRegular,
                            color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        }
    }
}

@Composable
private fun BookCoverPlaceholder(
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = MaterialTheme.shapes.small
                    )
                    .size(
                            width = COVER_WIDTH, height = COVER_HEIGHT
                    ), contentAlignment = Alignment.Center
    ) {
        Icon(
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = Color(0xFF7A7771),
                modifier = Modifier.size(BOOK_COVER_ICON_SIZE)
        )
    }
}

private val COVER_WIDTH = 104.dp
private val COVER_HEIGHT = 156.dp
private val MENU_BUBBLE_SIZE = IntSize(48, 48)
private val BOOK_COVER_ICON_SIZE = 56.dp

@Preview(showBackground = true, backgroundColor = 0xFFFDFCF8)
@Composable
private fun BookmarkCard_Preview() {
    PageKeeperTheme {

        val bookmarkItem = GlobalBookmarkUiItem(
                bookId = "id",
                title = "The Richest Man in Babylon",
                author = "George S. Clason",
                coverPath = null,
                bookmarkCount = 18
        )

        Column(modifier = Modifier.fillMaxSize()) {

            BookmarkCard(
                    item = bookmarkItem,
                    onItemClick = {},
                    selected = false,
                    isMenuExpanded = false,
                    modifier = Modifier,
                    onViewBookmarks = {},
                    onDeleteBookmarks = {},
                    onOpenContextMenu = {},
                    onDismissMenu = {})

            BookmarkCard(
                    item = bookmarkItem,
                    onItemClick = {},
                    selected = false,
                    isMenuExpanded = false,
                    modifier = Modifier,
                    onViewBookmarks = {},
                    onDeleteBookmarks = {},
                    onOpenContextMenu = {},
                    onDismissMenu = {})

            BookmarkCard(
                    item = bookmarkItem,
                    onItemClick = {},
                    selected = false,
                    isMenuExpanded = false,
                    modifier = Modifier,
                    onViewBookmarks = {},
                    onDeleteBookmarks = {},
                    onOpenContextMenu = {},
                    onDismissMenu = {})
        }
    }
}

