package com.tonyxlab.pagekeeper.presentation.screens.reader.read.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.core.components.ReadProgressBar
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadingOrientation
import com.tonyxlab.pagekeeper.presentation.theme.BgBottomNav
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.rememberIsMobileDevice
import kotlin.math.roundToInt

@Composable
fun ReaderBottomBar(
    uiState: ReaderUiState,
    onEvent: (ReaderUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val progress = uiState.book?.progress ?: 0f
    val progressText = (progress * 100).roundToInt()

    val showOrientationControl = rememberIsMobileDevice()

    val (orientationText, orientationIcon) =
        if (uiState.orientation == ReadingOrientation.AUTO_ROTATE)
            stringResource(id = R.string.bottom_text_auto_rotate) to
                    painterResource(id = R.drawable.ic_portrait)
        else
            stringResource(id = R.string.bottom_text_landscape) to
                    painterResource(id = R.drawable.ic_landscape)

    Column(
            modifier = modifier
                    .background(BgBottomNav)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceSmall)
                    .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.spaceSmall),
                text = stringResource(id = R.string.bottom_text_progress, progressText),
                style = MaterialTheme.typography.BodyMediumMedium,
                color = MaterialTheme.colorScheme.onSurface
        )

        ReadProgressBar(progress = progress)

        Row(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.spaceMedium),
                horizontalArrangement = Arrangement.Absolute.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
        ) {

            BottomBarItem(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.bottom_text_chapters),
                    painter = painterResource(id = R.drawable.ic_chapters)
            ) { onEvent(ReaderUiEvent.ViewChapters) }

            BottomBarItem(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.bottom_text_bookmarks),
                    painter = painterResource(id = R.drawable.ic_bookmark_big)
            ) { onEvent(ReaderUiEvent.ViewBookmarks) }

            if (showOrientationControl) {
                BottomBarItem(
                        modifier = Modifier.weight(1f),
                        text = orientationText,
                        painter = orientationIcon
                ) { onEvent(ReaderUiEvent.ToggleAutoRotate) }

            }
            BottomBarItem(
                    modifier = Modifier.weight(1f),
                    text = stringResource(id = R.string.bottom_text_font_size),
                    painter = painterResource(id = R.drawable.ic_font_size)
            ) { onEvent(ReaderUiEvent.FontSizeClicked) }

        }
    }
}

@Composable
private fun BottomBarItem(
    text: String,
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
            modifier = modifier
                    .clickable { onClick() },
            horizontalAlignment = Alignment.CenterHorizontally

    ) {
        IconButton(onClick = { onClick() }) {
            Icon(
                    painter = painter,
                    contentDescription = text,
                    tint = IconsTint
            )
        }
        Text(
                text = text,
                style = MaterialTheme.typography.BodySmallRegular,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                    uiState = ReaderUiState(),
                    onEvent = {}
            )
        }
    }
}
