package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.BgActive
import com.tonyxlab.pagekeeper.presentation.theme.BgMain
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumRegular
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.IconsTint
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.Primary
import com.tonyxlab.pagekeeper.presentation.theme.TextPrimary
import com.tonyxlab.pagekeeper.presentation.theme.TextSecondary
import com.tonyxlab.pagekeeper.presentation.theme.TitleLargeBold
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.ifThen

@Composable
fun EmptyBooksScreen(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    imageSize: Dp = 180.dp,
    iconSize: Dp = 100.dp,
    onImportBookClick: () -> Unit = {}
) {
    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(MaterialTheme.spacing.spaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Box(
                modifier = Modifier
                        .size(imageSize)
                        .background(BgActive, CircleShape),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    painter = painterResource(id = R.drawable.ic_book),
                    contentDescription = stringResource(id = R.string.cds_text_book),
                    modifier = Modifier.size(iconSize),
                    tint = IconsTint
            )
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceTwelve * 2))

        Text(
                text = stringResource(id = R.string.caption_text_empty_library),
                style = MaterialTheme.typography.TitleLargeBold,
                color = TextPrimary,
                textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

        Text(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceLarge),
                text = stringResource(id = R.string.caption_text_import_book),
                style = MaterialTheme.typography.BodyMediumRegular,
                color = TextSecondary,
                textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceLarge))

        Button(
                modifier = Modifier.height(MaterialTheme.spacing.spaceSmall * 7),
                onClick = onImportBookClick,
                colors = ButtonDefaults.buttonColors(
                        containerColor = Primary,
                        contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.spaceTwelve * 2)
        ) {
            Icon(
                    painter = painterResource(id = R.drawable.ic_import_book),
                    contentDescription = null,
                    tint = Color.White
            )
            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceSmall))
            Text(
                    text = stringResource(id = R.string.button_text_import_book),
                    style = MaterialTheme.typography.BodyMediumMedium
            )
        }
    }
}

@Composable
fun EmptyFavoritesScreen(
    isDeviceWide: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(176.dp))

        Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTwelve * 10)
                            .then(
                                    if (isDeviceWide) {
                                        Modifier.background(BgMain, CircleShape)
                                    } else {
                                        Modifier.background(BgActive, CircleShape)
                                    }
                            ),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.ic_star_big),
                        contentDescription = stringResource(id = R.string.cds_text_favorite),
                        modifier = Modifier.size(ICON_SIZE),
                        tint = IconsTint
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))

            Text(
                    text = stringResource(id = R.string.caption_text_favs_empty),
                    style = MaterialTheme.typography.TitleMediumMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

            Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceLarge),
                    text = stringResource(id = R.string.caption_text_favs_empty_desc),
                    style = MaterialTheme.typography.BodySmallRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
            )
        }
    }

}

@Composable
fun EmptyFinishedScreen(
    isDeviceWide: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {
    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(176.dp))
        Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                    modifier = Modifier
                            .size(MaterialTheme.spacing.spaceTwelve * 10)
                            .then(
                                    if (isDeviceWide) {
                                        Modifier.background(BgMain, CircleShape)
                                    } else {
                                        Modifier.background(BgActive, CircleShape)
                                    }
                            ),
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.ic_finished_big),
                        contentDescription = stringResource(id = R.string.cds_text_finished),
                        modifier = Modifier.size(ICON_SIZE),
                        tint = IconsTint
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))

            Text(
                    text = stringResource(id = R.string.caption_text_finished_empty),
                    style = MaterialTheme.typography.TitleMediumMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

            Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceLarge),
                    text = stringResource(id = R.string.caption_text_finished_empty_desc),
                    style = MaterialTheme.typography.BodySmallRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyBookmarkScreen(
    isDeviceWide: Boolean,
    modifier: Modifier = Modifier,
    isGlobalScreen: Boolean = false,
    backgroundColor: Color = MaterialTheme.colorScheme.background
) {

    val circleModifier = Modifier
            .size(MaterialTheme.spacing.spaceTwelve * 10)
            .then(
                    if (isDeviceWide) {
                        Modifier.background(BgMain, CircleShape)
                    } else {
                        Modifier.background(BgActive, CircleShape)
                    }
            )



    Column(
            modifier = modifier
                    .fillMaxSize()
                    .background(backgroundColor)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.height(176.dp))
        Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                    modifier = Modifier.ifThen(isGlobalScreen) {
                        circleModifier
                    },
                    contentAlignment = Alignment.Center
            ) {
                Icon(
                        painter = painterResource(id = R.drawable.ic_bookmark),
                        contentDescription = stringResource(id = R.string.cds_text_bookmark),
                        modifier = Modifier.size(ICON_SIZE),
                        tint = IconsTint
                )
            }

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))

            Text(
                    text = stringResource(id = R.string.caption_text_bookmarks_empty),
                    style = MaterialTheme.typography.TitleMediumMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceSmall))

            Text(
                    modifier = Modifier.padding(horizontal = MaterialTheme.spacing.spaceLarge),
                    text = stringResource(id = R.string.caption_text_bookmarks_empty_desc),
                    style = MaterialTheme.typography.BodySmallRegular,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
            )
        }
    }
}

private val ICON_SIZE = 72.dp

@Preview(showBackground = true)
@Composable
private fun EmptyBooksScreenPreview() {
    PageKeeperTheme {
        EmptyBooksScreen()
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyFavoritesScreenPreview() {
    PageKeeperTheme {
        EmptyFavoritesScreen(isDeviceWide = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyFinishedScreenPreview() {
    PageKeeperTheme {
        EmptyFinishedScreen(isDeviceWide = false)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyBookmarkScreenPreview() {
    PageKeeperTheme {
        EmptyBookmarkScreen(isDeviceWide = false)
    }
}


