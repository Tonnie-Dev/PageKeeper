package com.tonyxlab.pagekeeper.presentation.screens.chapters

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.chapters.handling.ChaptersActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.chapters.handling.ChaptersUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.chapters.handling.ChaptersUiState

typealias ChaptersBaseViewModel = BaseViewModel<ChaptersUiState, ChaptersUiEvent, ChaptersActionEvent>

class ChaptersViewModel : ChaptersBaseViewModel(initialState = ChaptersUiState()) {

    override fun onEvent(event: ChaptersUiEvent) = Unit
}
