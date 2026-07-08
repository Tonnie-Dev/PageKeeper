package com.tonyxlab.pagekeeper.presentation.screens.read

import androidx.compose.runtime.Composable
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReaderBottomBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReadScreen(
    viewModel: ReadViewModel = koinViewModel()
) {
    BaseContentLayout(
        viewModel = viewModel,
        bottomBar = { uiState ->
            ReaderBottomBar(
                uiState = uiState,
                onEvent = viewModel::onEvent
            )
        }
    ) {
    }
}