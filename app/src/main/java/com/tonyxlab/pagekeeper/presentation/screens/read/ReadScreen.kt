package com.tonyxlab.pagekeeper.presentation.screens.read

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReaderBottomBar
import com.tonyxlab.pagekeeper.presentation.theme.spacing
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
        ReadScreenContent()
    }
}

@Composable
fun ReadScreenContent(modifier: Modifier = Modifier) {
    Text(
            text = "",
            modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MaterialTheme.spacing.spaceTen * 2),
            style = MaterialTheme.typography.bodyLarge
    )
}