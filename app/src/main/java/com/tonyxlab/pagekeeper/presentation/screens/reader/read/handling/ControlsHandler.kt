package com.tonyxlab.pagekeeper.presentation.screens.reader.read.handling

import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadingControlMode
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadingOrientation

class ControlsHandler(
    private val currentState: () -> ReaderUiState,
    private val updateState: ((ReaderUiState) -> ReaderUiState) -> Unit,
    private val restartControlsAutoHideTimer: () -> Unit,
    private val cancelControlsAutoHideTimer: () -> Unit,
) {

    fun onReadingAreaClicked() {
        when (currentState().controlMode) {
            ReadingControlMode.Immersive -> showScreenControls()
            else -> hideScreenControls()
        }
    }

    fun showScreenControls() {
        updateState { state ->
            state.copy(controlMode = ReadingControlMode.DefaultToolbar)
        }
        restartControlsAutoHideTimer()
    }

    fun hideScreenControls() {
        cancelControlsAutoHideTimer()
        updateState { state ->
            state.copy(controlMode = ReadingControlMode.Immersive)
        }
    }

    fun onToggleAutoRotate() {
        updateState { state ->
            state.copy(
                    orientation = when (state.orientation) {
                        ReadingOrientation.AUTO_ROTATE -> ReadingOrientation.LANDSCAPE_LOCK
                        ReadingOrientation.LANDSCAPE_LOCK -> ReadingOrientation.AUTO_ROTATE
                    }
            )
        }
    }
}
