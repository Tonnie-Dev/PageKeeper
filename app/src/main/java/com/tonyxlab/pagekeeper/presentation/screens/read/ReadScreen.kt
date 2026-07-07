package com.tonyxlab.pagekeeper.presentation.screens.read

import androidx.compose.runtime.Composable
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import org.koin.androidx.compose.koinViewModel

@Composable
fun ReadScreen(
    viewModel: ReadViewModel = koinViewModel()
) {
    BaseContentLayout<ReadUiState, ReadUiEvent, ReadActionEvent>(
            viewModel = viewModel
    ) {
    }
}
