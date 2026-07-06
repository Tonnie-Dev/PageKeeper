package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.BookMock
import com.tonyxlab.pagekeeper.presentation.core.components.AppInputField
import com.tonyxlab.pagekeeper.presentation.core.components.LazyListComponent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.theme.BodyLargeRegular
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.TitleSmallMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(
    uiState: LibraryUiState,
    onEvent: (LibraryUiEvent) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean = uiState.searchState.searchTextFieldState.text.isNotBlank(),
    showBackButton: Boolean = true,
    showSearchIconWhenEmpty: Boolean = false,
    isDeviceWide: Boolean = false,
    inputHeight: Dp = MaterialTheme.spacing.spaceTwelve * 6,
) {
    val searchTextFieldState = uiState.searchState.searchTextFieldState
    val textFieldHasText = searchTextFieldState.text.isNotBlank()

    SearchBar(
            modifier = modifier,
            inputField = {
                Column(verticalArrangement = Arrangement.Top) {
                    AppInputField(
                            modifier = Modifier
                                    .clip(MaterialTheme.shapes.extraLarge)
                                    .weight(1f),
                            textFieldState = searchTextFieldState,
                            placeholderText = stringResource(R.string.placeholder_text_search),
                            height = inputHeight,
                            leadingIcon = if (showBackButton) {
                                {
                                    Image(
                                            modifier = Modifier.clickable(
                                                    onClick = {
                                                        onEvent(LibraryUiEvent.SearchBackClicked)
                                                    }
                                            ),
                                            painter = painterResource(R.drawable.ic_back),
                                            contentDescription = stringResource(id = R.string.cds_text_back),
                                    )
                                }
                            } else null,
                            trailingIcon = {
                                if (textFieldHasText) {
                                    Image(
                                            modifier = Modifier.clickable(onClick = {
                                                onEvent(
                                                        LibraryUiEvent.ClearSearchClicked
                                                )
                                            }),
                                            painter = painterResource(R.drawable.ic_cancel),
                                            contentDescription = stringResource(id = R.string.cds_text_back),
                                    )
                                } else if (showSearchIconWhenEmpty) {
                                    Column {
                                        Image(
                                                modifier = Modifier.clickable(
                                                        onClick = {
                                                            onEvent(LibraryUiEvent.SearchClicked)
                                                        }
                                                ),
                                                painter = painterResource(R.drawable.ic_search),
                                                contentDescription = stringResource(id = R.string.cds_text_search),
                                        )
                                    }
                                }
                            }
                    )
                    if (isDeviceWide.not()) {
                        HorizontalDivider(
                                color = Color(0xFFE1DDD0),
                                thickness = 1.dp
                        )
                    }
                }

            },
            expanded = expanded,
            onExpandedChange = {},
            colors = SearchBarDefaults.colors(
                    containerColor = if (isDeviceWide) TabletBlockBg else Color.Transparent,
                    dividerColor = Color.Transparent
            ),
    ) {

        val items = uiState.searchState.searchResults
        if (textFieldHasText && items.isEmpty()) {

            Box(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = MaterialTheme.spacing.spaceTen * 4),
                    contentAlignment = Alignment.Center
            ) {
                Text(
                        modifier = Modifier
                                .padding(MaterialTheme.spacing.spaceSmall),
                        text = stringResource(id = R.string.caption_text_no_results),
                        style = MaterialTheme.typography.TitleMediumMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        LazyListComponent(
                items = items,
                key = { item -> item.id },
                isDeviceWide = isDeviceWide
        ) { book ->
            SearchResultItem(book = book, modifier = Modifier)
        }
    }
}

@Composable
private fun SearchResultItem(
    book: Book,
    modifier: Modifier = Modifier,
) {
    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
                    .padding(
                            horizontal = MaterialTheme.spacing.spaceMedium,
                            vertical = MaterialTheme.spacing.spaceSmall
                    )
    ) {

        book.coverPath?.let { cover ->
            AsyncImage(
                    model = cover,
                    contentDescription = book.title,
                    modifier = Modifier
                            .background(color = MaterialTheme.colorScheme.surfaceVariant)
                            .size(width = 40.dp, height = 60.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
            )
        } ?: BookCoverPlaceholder()

        Column(
                modifier = Modifier
                        .weight(1f)
                        .padding(start = MaterialTheme.spacing.spaceMedium)
                        .fillMaxHeight()
        ) {
            Text(
                    text = book.title,
                    style = MaterialTheme.typography.TitleSmallMedium,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )

            Text(
                    text = book.author,
                    style = MaterialTheme.typography.BodySmallRegular,
                    color = TextSecondary,
                    modifier = Modifier.padding(top = MaterialTheme.spacing.spaceExtraSmall)
            )
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
                    .size(width = 40.dp, height = 60.dp)
                    .padding(MaterialTheme.spacing.spaceExtraSmall),
            contentAlignment = Alignment.Center
    ) {
        Icon(
                modifier = Modifier.size(MaterialTheme.spacing.spaceLargeMedium),
                painter = painterResource(id = R.drawable.ic_book),
                contentDescription = null,
                tint = Color(0xFF7A7771),
        )
    }
}

@Composable
fun WideDummySearchBar(onClick: () -> Unit) {
    Box(
            modifier = Modifier
                    .background(
                            MaterialTheme.colorScheme.surface,
                            shape = MaterialTheme.shapes.extraLarge
                    )
                    .height(40.dp)
                    .width(300.dp)
                    .clickable(onClick = onClick)
    ) {

        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.spaceTwelve * 2)
                        .padding(vertical = MaterialTheme.spacing.spaceSmall),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                    text = stringResource(id = R.string.placeholder_text_search),
                    style = MaterialTheme.typography.BodyLargeRegular,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = stringResource(id = R.string.cds_text_search)
            )
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchComponent_Preview() {

    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            SearchComponent(
                    uiState = LibraryUiState(

                            searchState = LibraryUiState.SearchState(
                                    searchTextFieldState = TextFieldState(initialText = "Tonnie"),
                                    isSearchMode = false,
                                    searchResults = BookMock.books
                            )
                    ),
                    onEvent = {}
            )
        }
    }
}
