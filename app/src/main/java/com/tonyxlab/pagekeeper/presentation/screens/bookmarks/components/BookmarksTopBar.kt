package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.screens.library.components.LibraryTopBar
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun BookmarksTopBar(
    showNavIcon: Boolean,
    modifier: Modifier = Modifier,
    onNavButtonClick: () -> Unit,
    onActionClick: () -> Unit
) {
    AppTopBar(
            modifier = modifier,
            titleText = stringResource(id = R.string.topbar_text_bookmarks),
            backgroundColor = MaterialTheme.colorScheme.surface,
            showNavIcon = showNavIcon,
            isLibraryScreen = true,
            onNavButtonClick = onNavButtonClick,
            actionIcon = {
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
                        tint = IconsTint
                )
            }
    )
}

@PreviewLightDark
@Composable
private fun BookmarksTopBarPreview() {
    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.background)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            BookmarksTopBar(
                    showNavIcon = true,
                    onNavButtonClick = {},
                    onActionClick = {}
            )
        }
    }
}


