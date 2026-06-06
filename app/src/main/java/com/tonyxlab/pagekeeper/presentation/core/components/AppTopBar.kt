package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    titleText: String,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onNavButtonClick: () -> Unit,
    onActionClick: () -> Unit
) {
    CenterAlignedTopAppBar(
            modifier = modifier,
            title = {
                Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )
            },
            navigationIcon = {

                Icon(
                        painter = painterResource(R.drawable.ic_menu),
                        contentDescription = stringResource(id = R.string.cds_text_menu),
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceLargeMedium)
                                .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.Button,
                                        onClick = onNavButtonClick
                                )
                                .padding(MaterialTheme.spacing.spaceTwelve)
                )
            },
            actions = {
                Icon(
                        painter = painterResource(R.drawable.ic_search),
                        contentDescription = stringResource(id = R.string.cds_text_search),
                        modifier = Modifier
                                .size(MaterialTheme.spacing.spaceLargeMedium)
                                .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.Button,
                                        onClick = onActionClick
                                )
                                .padding(MaterialTheme.spacing.spaceTwelve)
                )
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
        }
    }
}
