package com.tonyxlab.pagekeeper.utils

import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

enum class DeviceType {
    MOBILE_PORTRAIT,
    MOBILE_LANDSCAPE,
    TABLET_PORTRAIT,
    TABLET_LANDSCAPE,
    DESKTOP;

    companion object {

        fun fromWindowSizeClass(windowSizeClass: WindowSizeClass): DeviceType {

            val widthClass = windowSizeClass.widthSizeClass
            val heightClass = windowSizeClass.heightSizeClass

            return when {

                // Mobile Portrait (Compact width)
                widthClass == WindowWidthSizeClass.Compact &&
                        heightClass == WindowHeightSizeClass.Medium ->
                    MOBILE_PORTRAIT

                widthClass == WindowWidthSizeClass.Compact &&
                        heightClass == WindowHeightSizeClass.Expanded ->
                    MOBILE_PORTRAIT

                // Mobile Landscape
                widthClass == WindowWidthSizeClass.Expanded &&
                        heightClass == WindowHeightSizeClass.Compact ->
                    MOBILE_LANDSCAPE

                // Tablet Portrait
                widthClass == WindowWidthSizeClass.Medium &&
                        heightClass == WindowHeightSizeClass.Medium ->
                    TABLET_PORTRAIT

                // Tablet Landscape
                widthClass == WindowWidthSizeClass.Expanded &&
                        heightClass == WindowHeightSizeClass.Medium ->
                    TABLET_LANDSCAPE

                else -> DESKTOP
            }
        }
    }
}
