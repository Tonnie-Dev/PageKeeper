package com.tonyxlab.pagekeeper.presentation.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val PageKeeperColorScheme = lightColorScheme(
        primary = Primary,
        onPrimary = Color.White,

        secondary = BgCard,
        onSecondary = TextPrimary,

        background = BgMain,
        onBackground = TextPrimary,

        surface = BgMain,
        onSurface = TextPrimary,

        surfaceVariant = BgActive,
        onSurfaceVariant = TextSecondary,

        outline = Divider,

        error = StateAlert,
        onError = Color.White
)

@Composable
fun PageKeeperTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
            colorScheme = PageKeeperColorScheme,
            typography = Typography,
            shapes = MaterialShapes,
            content = content
    )
}