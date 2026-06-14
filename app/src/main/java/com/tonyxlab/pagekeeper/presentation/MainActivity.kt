package com.tonyxlab.pagekeeper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tonyxlab.pagekeeper.presentation.screens.library.LibraryScreen
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PageKeeperTheme {
                LibraryScreen()
            }
        }
    }
}
