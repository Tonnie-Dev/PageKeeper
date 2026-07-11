package com.tonyxlab.pagekeeper.presentation.screens.read

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReadTopBar
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReaderBottomBar
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadScreen(
    bookId: String,
    navigator: Navigator,
    viewModel: ReadViewModel = koinViewModel(parameters = { parametersOf(bookId) })
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    BaseContentLayout(
            viewModel = viewModel,
            onBackPressed = { navigator.popToLibrary() },
            topBar = {
                ReadTopBar(
                        titleText = uiState.book?.title ?: "",
                        isFavorite = uiState.book?.isFavorite ?: false,
                        modifier = Modifier,
                        onEvent = viewModel::onEvent
                )
            },
            bottomBar = { uiState ->
                ReaderBottomBar(
                        uiState = uiState,
                        onEvent = viewModel::onEvent
                )
            },
            actionEventHandler = { context, actionEvent ->
                when (actionEvent) {
                    is ReadActionEvent.ShowToast -> {
                        Toast.makeText(context, actionEvent.message, Toast.LENGTH_SHORT)
                                .show()
                    }
                    ReadActionEvent.ExitReader -> {
                        navigator.popToLibrary()
                    }
                }
            }
    ) { uiState ->
        ReadScreenContent(uiState = uiState)
    }
}

@Composable
fun ReadScreenContent(
    uiState: ReadUiState,
    modifier: Modifier = Modifier
) {
    val text = uiState.document?.blocks.orEmpty()
            .joinToString(separator = "\n\n") { block ->
                when (block) {
                    is ReaderContentBlock.ChapterTitle -> block.text
                    is ReaderContentBlock.Paragraph -> block.text.text
                    is ReaderContentBlock.Quote -> block.text.text
                }
            }

    Text(
            text = text,
            modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = MaterialTheme.spacing.spaceTen * 2),
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = uiState.fontSizeSp.sp)
    )
}


