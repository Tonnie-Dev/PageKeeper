package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.BookMock
import com.tonyxlab.pagekeeper.presentation.core.components.AppInputField
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.TitleSmallMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchComponent(
    state: LibraryUiState,
    modifier: Modifier = Modifier
) {
    val searchTextFieldState = state.searchState.searchTextFieldState
    val textFieldHasText = searchTextFieldState.text.isNotBlank()

    SearchBar(
            modifier = modifier.wrapContentHeight(),
            inputField = {
                Column {
                    AppInputField(
                            modifier = Modifier,
                            textFieldState = searchTextFieldState,
                            placeholderText = stringResource(R.string.placeholder_text_search),
                            leadingIcon = {
                                Column {

                                    Image(
                                            modifier = Modifier,
                                            painter = painterResource(R.drawable.ic_back),
                                            contentDescription = stringResource(id = R.string.cds_text_back),
                                    )
                                }
                            },
                            trailingIcon = {
                                if (textFieldHasText) {

                                    Image(
                                            modifier = Modifier,
                                            painter = painterResource(R.drawable.ic_cancel),
                                            contentDescription = stringResource(id = R.string.cds_text_back),
                                    )
                                }
                            }
                    )
                    HorizontalDivider(
                            color = Color(0xFFE1DDD0),
                            thickness = 1.dp
                    )
                }

            },
            //expanded = searchTextFieldState.text.isNotBlank(),
            expanded = true,
            onExpandedChange = {},
            colors = SearchBarDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    dividerColor = Color.Transparent
            ),
    ) {

        LazyColumn {

            val items = state.searchState.searchResults
            if (textFieldHasText && items.isEmpty()) {
                item {
                    Text(
                            modifier = Modifier.padding(MaterialTheme.spacing.spaceSmall),
                            text = stringResource(id = R.string.caption_text_no_results),
                            style = MaterialTheme.typography.titleSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                    )
                }
            }

            items(items = items, key = { it.id }) { book ->
                SearchResultItem(book = book, modifier = Modifier)
            }
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
                    .padding(MaterialTheme.spacing.spaceSmall)
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
                    state = LibraryUiState(

                            searchState = LibraryUiState.SearchState(
                                    searchTextFieldState = TextFieldState(initialText = "Tonnie"),

                                    searchResults = BookMock.books
                            )
                    )
            )

        }
    }
}
