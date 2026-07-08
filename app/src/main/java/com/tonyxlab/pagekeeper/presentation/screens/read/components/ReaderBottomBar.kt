package com.tonyxlab.pagekeeper.presentation.screens.read.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import com.tonyxlab.pagekeeper.presentation.theme.Icons
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun ReaderBottomBar(
    uiState: ReadUiState,
    onEvent: (ReadUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
            modifier = modifier.fillMaxWidth(),
            color = BgBottomNav,
            /* border = BorderStroke(
                 width = MaterialTheme.spacing.spaceSingleDp,
                 color = Divider
             )*/
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                                horizontal = MaterialTheme.spacing.spaceMedium,
                                vertical = MaterialTheme.spacing.spaceSmall
                        ),
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
                            painter = painterResource(
                                    id = if (uiState.orientation == ReadingOrientation.AUTO_ROTATE)
                                        R.drawable.ic_portrait
                                    else
                                        R.drawable.ic_landscape

                            ),
                            contentDescription = stringResource(id = R.string.bottom_text_auto_rotate),
                            tint = if (uiState.orientation == ReadingOrientation.AUTO_ROTATE) {
                                Primary
                            } else {
                                Icons
                            }
                    )
                }

                Text(

                        text = stringResource(id = R.string.bottom_text_auto_rotate),
                        style = MaterialTheme.typography.BodySmallRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(
                    modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { onEvent(ReadUiEvent.ChangeFontSize) }) {
                    Icon(
                            painter = painterResource(id = R.drawable.ic_font_size),
                            contentDescription = stringResource(id = R.string.bottom_text_font_size),
                            tint = Icons
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
        ReaderBottomBar(
                uiState = ReadUiState(),
                onEvent = {}
        )
    }
}