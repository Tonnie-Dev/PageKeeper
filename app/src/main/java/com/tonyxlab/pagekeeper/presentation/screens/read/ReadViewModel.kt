package com.tonyxlab.pagekeeper.presentation.screens.read

import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation

class ReadViewModel : BaseViewModel<ReadUiState, ReadUiEvent, ReadActionEvent>(
    initialState = ReadUiState()
) {

    override fun onEvent(event: ReadUiEvent) {
        when (event) {
            ReadUiEvent.ToggleAutoRotate -> onToggleAutoRotate()
            ReadUiEvent.ChangeFontSize -> onChangeFontSize()
        }
    }

    private fun onToggleAutoRotate() {
        updateState { state ->
            state.copy(
                orientation = when (state.orientation) {
                    ReadingOrientation.AUTO_ROTATE -> ReadingOrientation.LANDSCAPE_LOCK
                    ReadingOrientation.LANDSCAPE_LOCK -> ReadingOrientation.AUTO_ROTATE
                }
            )
        }
    }

    private fun onChangeFontSize() {
        updateState { state ->
            val currentIndex = FontSizeSteps.indexOf(state.fontSizeSp)
            val nextIndex = if (currentIndex == -1) {
                DefaultFontSizeIndex
            } else {
                (currentIndex + 1) % FontSizeSteps.size
            }

            state.copy(fontSizeSp = FontSizeSteps[nextIndex])
        }
    }

    private companion object {
        val FontSizeSteps = listOf(16f, 18f, 20f, 22f, 24f)
        const val DefaultFontSizeIndex = 1
    }
}