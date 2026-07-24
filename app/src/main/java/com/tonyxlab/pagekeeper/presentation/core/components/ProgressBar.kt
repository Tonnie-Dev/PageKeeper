package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme

/**
 * Displays determinate progress as a slim, rounded horizontal bar.
 *
 * Values outside the `0f..1f` range are coerced before being displayed.
 */
@Composable
fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    progressColor: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.outline,
    strokeWidth: Dp = 6.dp,
    trackGap: Dp = 8.dp,
) {
    val coercedProgress = progress.coerceIn(0f, 1f)

    LinearProgressIndicator(
            progress = { coercedProgress },
            modifier = modifier
                    .fillMaxWidth()
                    .height(strokeWidth),
            color = progressColor,
            trackColor = trackColor,
            strokeCap = StrokeCap.Round,
            gapSize = trackGap,
    )
}

@Preview(showBackground = true)
@Composable
private fun ProgressBarPreview() {
    PageKeeperTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.Center
        ) {
            ProgressBar(progress = 0.2f)
        }
    }
}
