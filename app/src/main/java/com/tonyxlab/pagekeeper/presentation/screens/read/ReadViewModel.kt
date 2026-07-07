package com.tonyxlab.pagekeeper.presentation.screens.read

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState

class ReadViewModel : BaseViewModel<ReadUiState, ReadUiEvent, ReadActionEvent>(
        initialState = ReadUiState()
) {

    override fun onEvent(event: ReadUiEvent) {
    }
}
