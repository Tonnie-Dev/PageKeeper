package com.tonyxlab.pagekeeper.presentation.screens.reader.read.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiEvent
import com.tonyxlab.pagekeeper.presentation.theme.BgBottomNav
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadTopBar(
    titleText: String,
    isFavorite: Boolean,
    modifier: Modifier = Modifier,
    onEvent: (ReaderUiEvent) -> Unit
) {
    AppTopBar(
            modifier = modifier,
            titleText = titleText,
            backgroundColor = BgBottomNav,
            onNavButtonClick = { onEvent(ReaderUiEvent.ExitReader) },
            actionIcon = {
                Icon(
                        modifier = Modifier
                                .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.Button,
                                        onClick = { onEvent(ReaderUiEvent.ToggleFavorite) }
                                )
                                .padding(MaterialTheme.spacing.spaceTwelve),
                        painter = painterResource(if (isFavorite) R.drawable.ic_star_filled else R.drawable.ic_star_outlined),
                        contentDescription = stringResource(id = R.string.cds_text_search),
                        tint = IconsTint
                )
            }
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

            ReadTopBar(
                    titleText = "The Richest Man in Babylon",
                    isFavorite = true,
                    onEvent = {}
            )

            ReadTopBar(
                    titleText = "The Richest Man in Babylon",
                    isFavorite = false,
                    onEvent = {}
            )
        }
    }
}

