package com.tonyxlab.pagekeeper.utils

import android.app.Activity
import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.IntSize
import androidx.core.view.WindowCompat
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadingOrientation

inline fun Modifier.ifThen(flag: Boolean, modifierBuilder: Modifier.() -> Modifier): Modifier =
    if (flag) this.modifierBuilder() else this

@Composable
fun SetStatusBarIconsColor(darkIcons: Boolean) {

    val view = LocalView.current

    val insetsController = remember(view) {
        (view.context as? Activity)
                ?.window
                ?.let { WindowCompat.getInsetsController(it, it.decorView) }
    }

    SideEffect {
        insetsController?.isAppearanceLightStatusBars = darkIcons
    }
}

@Composable
fun IntSize.toDpSize(): DpSize {
    return DpSize(
            width = with(LocalDensity.current) { width.toDp() },
            height = with(LocalDensity.current) { height.toDp() }
    )
}

@Composable
fun InvisibleSpacer(
    componentSize: IntSize,
    modifier: Modifier = Modifier
) {
    Spacer(
            modifier = modifier
                    .size(with(LocalDensity.current) {
                        componentSize.toDpSize()
                    }
                    )
    )
}

@Composable
fun Modifier.clickableWithoutRipple(onClick: () -> Unit): Modifier =
    this.then(Modifier.pointerInput(Unit) {
        detectTapGestures(onTap = { onClick }
        )
    }
    )

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun rememberIsDeviceWide(): Boolean {
    val activity = LocalActivity.current ?: return false
    val windowClass = calculateWindowSizeClass(activity)
    val deviceType = DeviceType.fromWindowSizeClass(windowClass)
    return remember(windowClass) { deviceType != DeviceType.MOBILE_PORTRAIT }
}


@Composable
fun ReaderOrientationEffect(
    orientation: ReadingOrientation,
    isMobileDevice: Boolean,
) {
    val activity = LocalActivity.current ?: return

    LaunchedEffect(
            activity,
            orientation,
            isMobileDevice,
    ) {
        activity.requestedOrientation =
            if (isMobileDevice) {
                when (orientation) {
                    ReadingOrientation.AUTO_ROTATE ->
                        ActivityInfo.SCREEN_ORIENTATION_FULL_USER

                    ReadingOrientation.LANDSCAPE_LOCK ->
                        ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
                }
            } else {
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            }
    }

    DisposableEffect(activity) {
        onDispose {
            activity.requestedOrientation =
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        }
    }
}
@Composable
fun rememberIsMobileDevice(): Boolean {
    val configuration = LocalConfiguration.current

    return configuration.smallestScreenWidthDp < 600
}
