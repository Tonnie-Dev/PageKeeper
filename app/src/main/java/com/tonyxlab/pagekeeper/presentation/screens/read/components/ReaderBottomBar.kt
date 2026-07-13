package com.tonyxlab.pagekeeper.presentation.screens.read.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation
import com.tonyxlab.pagekeeper.presentation.theme.BgBottomNav
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun ReaderBottomBar(
    uiState: ReadUiState,
    onEvent: (ReadUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val (orientationText, orientationIcon) =
        if (uiState.orientation == ReadingOrientation.AUTO_ROTATE)
            stringResource(id = R.string.bottom_text_auto_rotate) to
                    painterResource(id = R.drawable.ic_portrait)
        else
            stringResource(id = R.string.bottom_text_landscape) to
                    painterResource(id = R.drawable.ic_landscape)

    BottomAppBar(modifier = modifier, containerColor = BgBottomNav) {

        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { onEvent(ReadUiEvent.ToggleAutoRotate) }) {
                    Icon(
                            painter = orientationIcon,
                            contentDescription = orientationText,
                            tint = if (uiState.orientation == ReadingOrientation.AUTO_ROTATE) {
                                Primary
                            } else {
                                IconsTint
                            }
                    )
                }

                Text(
                        text = orientationText,
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { onEvent(ReadUiEvent.FontSizeClicked) }) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_font_size),
                            contentDescription = stringResource(id = R.string.bottom_text_font_size),
                            tint = IconsTint
                    )
                }
                Text(
                        text = stringResource(id = R.string.bottom_text_font_size),
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ReaderBottomBarPreview() {
    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceOneHundred)
        ) {
            ReaderBottomBar(
                    uiState = ReadUiState(),
                    onEvent = {}
            )
        }
    }
}
