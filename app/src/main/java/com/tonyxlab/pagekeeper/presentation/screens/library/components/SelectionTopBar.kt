package com.tonyxlab.pagekeeper.presentation.screens.library.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectionTopBar(
    selectedCount: Int,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onBackClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit
) {

    AppTopBar(
            modifier = modifier,
            titleText = stringResource(
                    id = R.string.topbar_text_selected_count,
                    selectedCount
            ),
            backgroundColor = backgroundColor,
            textAlign = TextAlign.Start,
            onNavButtonClick = onBackClick,
            actionIcon = {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_star_outlined),
                            contentDescription = stringResource(id = R.string.cds_text_favorite),
                            tint = IconsTint
                    )
                }

                IconButton(onClick = onShareClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = stringResource(id = R.string.cds_text_share),
                            tint = IconsTint
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(id = R.string.cds_text_delete),
                            tint = IconsTint
                    )
                }
            }
    )
}

@PreviewLightDark
@Composable
private fun LibraryTopBarPreview() {
    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            SelectionTopBar(
                    selectedCount = 2,
                    backgroundColor = TabletBlockBg,
                    onBackClick = {},
                    onFavoriteClick = {},
                    onShareClick = {},
                    onDeleteClick = {}
            )
        }
    }
}

