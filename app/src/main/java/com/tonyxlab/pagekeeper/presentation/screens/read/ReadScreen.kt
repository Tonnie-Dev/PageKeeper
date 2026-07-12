package com.tonyxlab.pagekeeper.presentation.screens.read

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.data.model.ReaderBlock
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ChapterTitleBlock
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ParagraphBlock
import com.tonyxlab.pagekeeper.presentation.screens.read.components.QuoteBlock
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReadTopBar
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReaderBottomBar
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import timber.log.Timber

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

    val blocks = uiState.document?.blocks.orEmpty()

   blocks. forEachIndexed { index, block ->
        Timber.d(
                "Block[$index]: ${block::class.simpleName} -> ${
                    when (block) {
                        is ReaderBlock.ChapterTitle -> block.text
                        is ReaderBlock.Paragraph -> block.text.text
                        is ReaderBlock.Quote -> block.text.text
                        else -> {}
                    }
                }"
        )
    }

    LazyColumn(
            modifier = modifier.fillMaxWidth(),
            contentPadding = PaddingValues(
                    horizontal = MaterialTheme.spacing.spaceTen * 2,
                    vertical = MaterialTheme.spacing.spaceTen * 2
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTen * 2)
    ) {

        items(items = blocks){ block ->

            when(block){

                is ReaderContentBlock.ChapterTitle -> {

                    ChapterTitleBlock(
                            block = block,
                            fontSizeSp = uiState.fontSizeSp
                    )

                }
                is ReaderContentBlock.Paragraph -> {

                    ParagraphBlock(
                            block = block,
                            fontSizeSp = uiState.fontSizeSp
                    )

                }
                is ReaderContentBlock.Quote -> {
                    QuoteBlock(
                            block = block,
                            fontSizeSp = uiState.fontSizeSp
                    )

                }
            }
        }
    }

}


