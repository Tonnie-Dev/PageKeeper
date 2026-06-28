package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.Icons
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titleText: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    showNavigationIcon: Boolean = true,
    onNavButtonClick: () -> Unit,
    onActionClick: () -> Unit
) {
    CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                        text = titleText,
                        style = MaterialTheme.typography.TitleMediumMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {
                if (showNavigationIcon) {
                    Icon(
                            modifier = Modifier
                                    .clickable(
                                            interactionSource = remember { MutableInteractionSource() },
                                            indication = null,
                                            role = Role.Button,
                                            onClick = onNavButtonClick
                                    )
                                    .padding(MaterialTheme.spacing.spaceTwelve),
                            contentDescription = stringResource(id = R.string.cds_text_menu),
                            painter = painterResource(R.drawable.ic_menu),
                            tint = Icons
                    )
                }
            },
            actions = {
                Icon(
                        modifier = Modifier
                                .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.Button,
                                        onClick = onActionClick
                                )
                                .padding(MaterialTheme.spacing.spaceTwelve),
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(id = R.string.cds_text_search),
                        tint = Icons
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
            )
    )
}

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
    TopAppBar(
            modifier = modifier,
            title = {
                Text(
                        text = "$selectedCount selected",
                        style = MaterialTheme.typography.TitleMediumMedium,
                        color = TextPrimary
                )
            },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = stringResource(id = R.string.cds_text_back),
                            tint = Icons
                    )
                }
            },
            actions = {
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_star_outlined),
                            contentDescription = stringResource(id = R.string.cds_text_favorite),
                            tint = Icons
                    )
                }

                IconButton(onClick = onShareClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_share),
                            contentDescription = stringResource(id = R.string.cds_text_share),
                            tint = Icons
                    )
                }

                IconButton(onClick = onDeleteClick) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_delete),
                            contentDescription = stringResource(id = R.string.cds_text_delete),
                            tint = Icons
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor
            )
    )
}

@PreviewLightDark
@Composable
private fun AppTopBarPreview() {
    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            AppTopBar(
                    titleText = stringResource(id = R.string.topbar_text_library),
                    backgroundColor = TabletBlockBg,
                    onNavButtonClick = {},
                    onActionClick = {}
            )

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

