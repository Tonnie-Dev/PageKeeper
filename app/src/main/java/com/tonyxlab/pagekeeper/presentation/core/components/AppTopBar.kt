@file:OptIn(ExperimentalMaterial3Api::class)

package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun AppTopBar(
    titleText: String,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = MaterialTheme.typography.TitleMediumMedium,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    showNavIcon: Boolean = true,
    isLibraryScreen: Boolean = false,
    textAlign: TextAlign = TextAlign.Center,
    onNavButtonClick: () -> Unit,
    actionIcon: @Composable RowScope.() -> Unit
) {
    CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = titleText,
                        style = titleStyle,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = textAlign,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                )
            },
            navigationIcon = {
                if (showNavIcon) {
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
                            painter = painterResource(
                                    id = if (isLibraryScreen)
                                        R.drawable.ic_menu
                                    else
                                        R.drawable.ic_back
                            ),
                            tint = IconsTint
                    )
                }
            },
            actions = { actionIcon() },
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
                    isLibraryScreen = true,
                    onNavButtonClick = {},
                    actionIcon = {}
            )
        }
    }
}

