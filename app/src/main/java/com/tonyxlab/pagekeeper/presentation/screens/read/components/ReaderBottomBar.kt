package com.tonyxlab.pagekeeper.presentation.screens.read.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation
import com.tonyxlab.pagekeeper.presentation.theme.BgBottomNav
import com.tonyxlab.pagekeeper.presentation.theme.BgMain
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import kotlin.math.roundToInt

@Composable
fun ReaderBottomBar(
    uiState: ReadUiState,
    onEvent: (ReadUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    BottomAppBar(modifier = modifier, containerColor = BgBottomNav) {

        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                                horizontal = MaterialTheme.spacing.spaceMedium,
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
                                IconsTint
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

@Composable
fun ReaderSliderBar(
    uiState: ReadUiState,
    onEvent: (ReadUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
            modifier = modifier.fillMaxWidth(),
            color = BgBottomNav
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                                horizontal = MaterialTheme.spacing.spaceLarge,
                                vertical = MaterialTheme.spacing.spaceTen
                        ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) {
            FontSizeButton(

                    icon = {
                        Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = stringResource(
                                        id = R.string.bottom_text_decrease_font_size
                                ),
                                tint = BgMain
                        )
                    },
                    onClick = { onEvent(ReadUiEvent.DecreaseFontSize) }
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            FontSizeSlider(
                    fontSizeSp = uiState.fontSizeSp,
                    onFontSizeChange = { onEvent(ReadUiEvent.SetFontSize(it)) },
                    modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceTwelve))

            FontSizeButton(
                    icon = {
                        Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(
                                        id = R.string.bottom_text_increase_font_size
                                ),
                                tint = BgMain
                        )
                    },
                    onClick = { onEvent(ReadUiEvent.IncreaseFontSize) }
            )
        }
    }
}

@Composable
private fun FontSizeSlider(
    fontSizeSp: Float,
    onFontSizeChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
            modifier = modifier.height(SliderHeight),
            contentAlignment = Alignment.BottomCenter
    ) {
        val fraction = (fontSizeSp - MinFontSizeSp) / (MaxFontSizeSp - MinFontSizeSp)
        val bubbleOffset = (maxWidth - BubbleSize) * fraction.coerceIn(0f, 1f)

        Surface(
                modifier = Modifier
                        .offset(x = bubbleOffset)
                        .size(BubbleSize)
                        .align(Alignment.TopStart),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 3.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                        text = fontSizeSp.roundToInt()
                                .toString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp
                )
            }
        }

        Slider(
                value = fontSizeSp,
                onValueChange = onFontSizeChange,
                valueRange = MinFontSizeSp..MaxFontSizeSp,
                steps = FontSizeSliderSteps,
                modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                colors = SliderDefaults.colors(
                        thumbColor = Primary,
                        activeTrackColor = Primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
                )
        )
    }
}

@Composable
private fun FontSizeButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
            modifier = modifier
                    .size(width = 40.dp, height = 32.dp)
                    .clip(RoundedCornerShape(100))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
    ) {
        icon()
    }

}

private val FontSizeButtonSize = 38.dp
private val SliderHeight = 58.dp
private val BubbleSize = 50.dp
private const val MinFontSizeSp = 16f
private const val MaxFontSizeSp = 24f
private const val FontSizeSliderSteps = 1

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

            ReaderSliderBar(

                    uiState = ReadUiState(),
                    onEvent = {}
            )
        }
    }
}
