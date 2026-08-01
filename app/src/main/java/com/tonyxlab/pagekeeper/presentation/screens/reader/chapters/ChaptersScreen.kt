package com.tonyxlab.pagekeeper.presentation.screens.reader.chapters

import androidx.annotation.Keep
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
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.core.components.AppTopBar
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.components.ChaptersList
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.util.findCurrentChapter
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.util.findCurrentSectionIndex
import com.tonyxlab.pagekeeper.presentation.theme.TabletBlockBg
import kotlin.text.Typography.section

@Composable
fun ChaptersScreen(
    viewModel: ReadViewModel,
    navigateToReadScreen: () -> Unit
) {

    BaseContentLayout(
            viewModel = viewModel,
            onBackPressed = { navigateToReadScreen() },
            topBar = {
                AppTopBar(
                        titleText = stringResource(id = R.string.topbar_text_chapters),
                        backgroundColor = TabletBlockBg,
                        onNavButtonClick = { viewModel.onEvent(ReaderUiEvent.BackClicked) }

                )
            },
            actionEventHandler = { _, actionEvent ->
                when (actionEvent) {
                    ReaderActionEvent.CloseChapters -> navigateToReadScreen()
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
    uiState: ReaderUiState,
    onEvent: (ReaderUiEvent) -> Unit,
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
                onEvent(ReaderUiEvent.ChapterSelected(item.startBlockIndex))
            },
    )
}
