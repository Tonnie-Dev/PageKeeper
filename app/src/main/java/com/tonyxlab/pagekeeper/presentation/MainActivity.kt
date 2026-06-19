package com.tonyxlab.pagekeeper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.tonyxlab.pagekeeper.presentation.screens.library.LibraryScreen
import com.tonyxlab.pagekeeper.presentation.screens.library.LibraryViewModel
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {


    private val libraryViewModel: LibraryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().setKeepOnScreenCondition {
            libraryViewModel.uiState.value.isLoading
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PageKeeperTheme {
                LibraryScreen(viewModel = libraryViewModel)
            }
        }
    }
}
