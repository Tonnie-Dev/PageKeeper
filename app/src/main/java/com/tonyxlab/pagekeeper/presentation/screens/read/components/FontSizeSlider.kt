@file:OptIn(ExperimentalMaterial3Api::class)

package com.tonyxlab.pagekeeper.presentation.screens.read.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderColors
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReaderFontSize
import com.tonyxlab.pagekeeper.presentation.theme.BgMain
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FontSizeControlPanel(
    fontSize: Float,
    onFontSizeChange: (Float) -> Unit,
    onFontSizeChangeFinished: (Float) -> Unit,
    modifier: Modifier = Modifier
) {

    var sliderValue by rememberSaveable {
        mutableFloatStateOf(fontSize.coerceIn(ReaderFontSize.range))
    }

    LaunchedEffect(fontSize) {
        sliderValue = fontSize.coerceIn(ReaderFontSize.range)
    }

    Surface(
            modifier = modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                                horizontal = 24.dp,
                                vertical = 20.dp,
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            FontSizeAdjustmentButton(

                    enabled = sliderValue > ReaderFontSize.MIN,
                    icon = {
                        Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = stringResource(
                                        id = R.string.bottom_text_decrease_font_size
                                ),
                                tint = BgMain
                        )
                    },
                    onClick = {


                        val newValue = (sliderValue - 1f)
                                .coerceAtLeast(ReaderFontSize.MIN)

                        sliderValue = newValue
                        onFontSizeChange(newValue)
                        onFontSizeChangeFinished(newValue)
                    }
            )


            FontSizeSlider(
                    value = sliderValue,
                    onValueChange = { newValue ->
                        sliderValue = newValue
                        onFontSizeChange(newValue)
                    },
                    onValueChangeFinished = {
                        onFontSizeChangeFinished(sliderValue)
                    },
                    modifier = Modifier.weight(1f),
            )

            FontSizeAdjustmentButton(

                    enabled = sliderValue > ReaderFontSize.MIN,
                    icon = {
                        Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(
                                        id = R.string.bottom_text_increase_font_size
                                ),
                                tint = BgMain
                        )
                    },
                    onClick = {


                        val newValue = (sliderValue + 1f)
                                .coerceAtMost(ReaderFontSize.MAX)

                        sliderValue = newValue
                        onFontSizeChange(newValue)
                        onFontSizeChangeFinished(newValue)
                    }
            )


        }
    }

}


@Composable
private fun FontSizeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val sliderColors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            activeTickColor = MaterialTheme.colorScheme.primary,
            inactiveTickColor = MaterialTheme.colorScheme.primary,
    )

    Box(
            modifier = modifier.height(112.dp),
            contentAlignment = Alignment.BottomCenter,
    ) {
        Slider(
                value = value,
                onValueChange = {
                    onValueChange(
                            it.coerceIn(ReaderFontSize.range)
                    )
                },
                onValueChangeFinished = onValueChangeFinished,
                valueRange = ReaderFontSize.range,

                /*
                 * Zero means continuous adjustment.
                 * We are not forcing the slider to jump by exactly 1 sp.
                 */
                steps = 0,

                interactionSource = interactionSource,
                colors = sliderColors,
                modifier = Modifier.fillMaxWidth(),

                thumb = {
                    FontSizeSliderThumb(
                            value = value,
                            interactionSource = interactionSource,
                            colors = sliderColors,
                    )
                },

                track = { sliderState ->
                    SliderDefaults.Track(
                            sliderState = sliderState,
                            colors = sliderColors,
                            thumbTrackGapSize = 8.dp,
                            trackInsideCornerSize = 8.dp,
                    )
                },
        )
    }
}

@Composable
private fun FontSizeSliderThumb(
    value: Float,
    interactionSource: MutableInteractionSource,
    colors: SliderColors,
) {
    Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-26).dp),
    ) {
        Surface(
                modifier = Modifier.size(76.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 10.dp,
                tonalElevation = 2.dp,
        ) {
            Box(
                    contentAlignment = Alignment.Center,
            ) {
                Text(
                        text = value.roundToInt().toString(),
                        style = MaterialTheme.typography.displaySmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        SliderDefaults.Thumb(
                interactionSource = interactionSource,
                colors = colors,
                modifier = Modifier
                        .width(12.dp)
                        .height(52.dp),
        )
    }
}

@Composable
private fun FontSizeAdjustmentButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {

    Box(
            modifier = modifier
                    .size(width = 40.dp, height = 32.dp)
                    .clip(RoundedCornerShape(100))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center
    ) {
        icon()
    }

}


/*
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

*/
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

            FontSizeControlPanel(
                    fontSize = 18.0f,
                    onFontSizeChange = {},
                    onFontSizeChangeFinished = {},


            )
        }
    }
}
