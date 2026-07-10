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
            is ReadUiEvent.SetFontSize -> onChangeFontSize(event.fontSizeSp)
            ReadUiEvent.DecreaseFontSize -> TODO()
            ReadUiEvent.IncreaseFontSize -> TODO()
            ReadUiEvent.ChangeFontSize -> TODO()
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

    /* private fun onChangeFontSize() {
         updateState { state ->
             val currentIndex = FontSizeSteps.indexOf(state.fontSizeSp)
             val nextIndex = if (currentIndex == -1) {
                 DefaultFontSizeIndex
             } else {
                 (currentIndex + 1) % FontSizeSteps.size
             }

             state.copy(fontSizeSp = FontSizeSteps[nextIndex])
         }
     }*/

    private fun onDecreaseFontSize() {
        updateState { state ->
            state.copy(fontSizeSp = (state.fontSizeSp - FontSizeStep).coerceInFontRange())
        }
    }

    private fun onIncreaseFontSize() {
        updateState { state ->
            state.copy(fontSizeSp = (state.fontSizeSp + FontSizeStep).coerceInFontRange())
        }
    }

    private fun onChangeFontSize(fontSizeSp: Float) {
        updateState { state ->
            state.copy(fontSizeSp = fontSizeSp.coerceInFontRange())
        }

    }

    private fun Float.coerceInFontRange(): Float {
        return coerceIn(MinFontSizeSp, MaxFontSizeSp)
                .toInt()
                .toFloat()
    }

    private companion object {
        const val MinFontSizeSp = 16f
        const val MaxFontSizeSp = 24f
        const val FontSizeStep = 1f
        val FontSizeSteps = listOf(16f, 18f, 20f, 22f, 24f)
        const val DefaultFontSizeIndex = 1
    }
}