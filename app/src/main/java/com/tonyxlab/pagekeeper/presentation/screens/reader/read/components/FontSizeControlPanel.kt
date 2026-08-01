@file:OptIn(ExperimentalMaterial3Api::class)

package com.tonyxlab.pagekeeper.presentation.screens.reader.read.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderFontSize
import com.tonyxlab.pagekeeper.presentation.theme.BgBottomNav
import com.tonyxlab.pagekeeper.presentation.theme.BgMain
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TitleLargeBold
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import kotlin.math.roundToInt

@Composable
fun FontSizeControlPanel(
    fontSize: Float,
    onEvent: (ReaderUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
            modifier = modifier
                    .fillMaxWidth()
                    .background(color = BgBottomNav)
    ) {
        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .padding(top = 28.dp, bottom = 20.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            FontSizeAdjustmentButton(
                    enabled = fontSize > ReaderFontSize.MIN,
                    icon = {
                        Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = stringResource(
                                        id = R.string.bottom_text_decrease_font_size
                                ),
                                tint = it
                        )
                    },
                    onClick = { onEvent(ReaderUiEvent.ViewChapters) }
            )

            FontSizeSlider(
                    value = fontSize,
                    onValueChange = { newValue ->
                        onEvent(
                                ReaderUiEvent.PreviewFontSizeChange(newValue)
                        )
                    },
                    onValueChangeFinished = {
                        onEvent(ReaderUiEvent.FontSizeChangeFinished)
                    },
                    modifier = Modifier.weight(1f),
            )

            FontSizeAdjustmentButton(
                    enabled = fontSize < ReaderFontSize.MAX,
                    icon = {
                        Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(
                                        id = R.string.bottom_text_increase_font_size
                                ),
                                tint = it
                        )
                    },
                    onClick = { onEvent(ReaderUiEvent.IncreaseFontSize) }
            )
        }
    }
}

@Composable
private fun FontSizeSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit,
    modifier: Modifier = Modifier
) {

    val interactionSource = remember { MutableInteractionSource() }

    val sliderColors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            activeTickColor = MaterialTheme.colorScheme.primary,
            inactiveTickColor = MaterialTheme.colorScheme.primary,
    )

    // Use Box with Constraints to calculate the exact width of the track
    // to position of the floating bubble horizontally

    BoxWithConstraints(
            modifier = modifier.height(SliderHeight),
            contentAlignment = Alignment.BottomCenter
    ) {

        val minVal = ReaderFontSize.MIN
        val maxVal = ReaderFontSize.MAX

        val range = maxVal - minVal

        // Calculate fraction (0.0 to 1.0) of how far along the slider is
        val sliderFraction = if (range > 0f) ((value - minVal) / range)
                .coerceIn(0f, 1f)
        else
            0f

        // Account for the bubble size to center nicely above the thumb
        val thumbCenterX =
            (SliderWidth / 2) +
                    ((maxWidth - SliderWidth) * sliderFraction)

        val bubbleOffsetX =
            thumbCenterX - (BubbleSize.width / 2)

        Surface(
                modifier = Modifier
                        .align(Alignment.TopStart)
                        .offset(
                                x = bubbleOffsetX,
                                y = (-54).dp
                        )
                        .size(BubbleSize),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp,
                tonalElevation = 2.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                        text = value.roundToInt()
                                .toString(),
                        style = MaterialTheme.typography.TitleLargeBold,
                        color = MaterialTheme.colorScheme.onSurface,
                )
            }
        }

        Slider(
                modifier = Modifier.fillMaxWidth(),
                value = value,
                onValueChange = onValueChange,
                onValueChangeFinished = onValueChangeFinished,
                valueRange = ReaderFontSize.range,
                steps = 0,
                interactionSource = interactionSource,
                colors = sliderColors,
                thumb = {
                    Box(
                            modifier = Modifier
                                    .width(SliderWidth)
                                    .height(SliderHeight)
                                    .background(
                                            color = MaterialTheme.colorScheme.primary,
                                            shape = MaterialTheme.shapes.extraSmall
                                    )
                    )
                },
                track = { sliderState ->
                    SliderDefaults.Track(
                            sliderState = sliderState,
                            colors = sliderColors,
                            thumbTrackGapSize = MaterialTheme.spacing.spaceSmall,
                            trackInsideCornerSize = MaterialTheme.spacing.spaceDefault
                    )
                }
        )
    }
}

@Composable
private fun FontSizeAdjustmentButton(
    icon: @Composable (Color) -> Unit,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val (background, tint) = if (enabled) {
        MaterialTheme.colorScheme.primary to BgMain
    } else {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.38f) to
                MaterialTheme.colorScheme.onPrimary
    }

    Box(
            modifier = modifier
                    .size(FontAdjustmentButtonSize)
                    .clip(RoundedCornerShape(100))
                    .background(color = background)
                    .clickable(enabled = enabled, onClick = onClick),
            contentAlignment = Alignment.Center
    ) {
        icon(tint)
    }
}

private val SliderWidth = 6.dp
private val SliderHeight = 44.dp
private val BubbleSize = DpSize(64.dp, 54.dp)
private val FontAdjustmentButtonSize = DpSize(40.dp, 32.dp)

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
                    uiState = ReaderUiState(),
                    onEvent = {}
            )

            FontSizeControlPanel(
                    fontSize = 18.0f,
                    onEvent = {}
            )
        }
    }
}
