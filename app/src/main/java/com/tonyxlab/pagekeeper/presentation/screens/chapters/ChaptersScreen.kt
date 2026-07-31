package com.tonyxlab.pagekeeper.presentation.screens.chapters

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.findCurrentChapter
import com.tonyxlab.pagekeeper.domain.model.findCurrentSectionIndex
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.chapters.components.ChaptersList
import com.tonyxlab.pagekeeper.presentation.screens.read.ReadViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ChaptersScreen(
    bookId: String,
    navigator: Navigator,
    viewModel: ReadViewModel = koinViewModel(parameters = { parametersOf(bookId) })
) {

    BaseContentLayout(
            viewModel = viewModel,
            onBackPressed = navigator::popToLibrary,
            topBar = {
                AppTopBar(
                        titleText = stringResource(id = R.string.topbar_text_chapters),
                        backgroundColor = TabletBlockBg,
                        onNavButtonClick = {viewModel.onEvent(ReadUiEvent.BackClicked)}

                )
            },
            actionEventHandler = { _, actionEvent ->
                when (actionEvent) {

                    ReadActionEvent.CloseChapters -> navigator.popToLibrary()
                    else -> Unit
                }

            }
    ) { state ->
        ChaptersScreenContent(
                uiState = state,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun ChaptersScreenContent(
    uiState: ReadUiState,
    onEvent: (ReadUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val sections = uiState.chapterSections
    val currentBlockIndex = uiState.currentBlockIndex

    val currentChapter = remember(
            sections,
            currentBlockIndex
    ) {
        sections.findCurrentChapter(currentBlockIndex)
    }

    val initialExpandedSectionIndex = remember(
            sections,
            currentChapter
    ) {
        sections.findCurrentSectionIndex(
                currentChapterId = currentChapter?.id
        )
    }

    var expandedSectionIndex by rememberSaveable(
            initialExpandedSectionIndex
    ) {
        mutableIntStateOf(initialExpandedSectionIndex)
    }
    ChaptersList(
            modifier = modifier
                    .fillMaxSize()
                    .padding(),
            sections = sections,
            expandedIndex = expandedSectionIndex,
            currentChapterId = currentChapter?.id,
            onSectionClick = { selectedSectionIndex ->
                expandedSectionIndex =
                    if (
                        expandedSectionIndex ==
                        selectedSectionIndex
                    ) {
                        /*
                         * Keep it expanded. This guarantees that one
                         * section remains open.
                         */
                        selectedSectionIndex
                    } else {
                        selectedSectionIndex
                    }
            },
            onChapterClick = { item ->
                onEvent(ReadUiEvent.ChapterSelected(item.startBlockIndex))
            },
    )
}
