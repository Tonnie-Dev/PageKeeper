package com.tonyxlab.pagekeeper.presentation.screens.reader.read.handling

import com.tonyxlab.pagekeeper.data.local.datastore.FontDataStore
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderFontSize
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadingControlMode
import com.tonyxlab.pagekeeper.presentation.screens.reader.coercedFontSize
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FontHandler(
    private val fontDataStore: FontDataStore,
    private val coroutineScope: CoroutineScope,
    private val currentState: () -> ReaderUiState,
    private val updateState: ((ReaderUiState) -> ReaderUiState) -> Unit,
    private val restartControlsAutoHideTimer: () -> Unit,
    private val onSaveError: () -> Unit,
) {

    fun loadFontSize() {
        coroutineScope.launch {
            val savedFontSize = fontDataStore.fontSize.first()
            updateState { state ->
                state.copy(
                        fontSizeState = state.fontSizeState.copy(
                                fontSize = savedFontSize,
                                previewFontSize = savedFontSize
                        )
                )
            }
        }
    }

    fun showFontSizePanel() {
        updateState { state ->
            state.copy(
                    controlMode = ReadingControlMode.FontSizePanel,
                    fontSizeState = state.fontSizeState.copy(
                            previewFontSize = state.fontSizeState.fontSize
                    )
            )
        }
        restartControlsAutoHideTimer()
    }

    fun previewFontSize(fontSize: Float) {
        updateState { state ->
            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            previewFontSize = fontSize.coercedFontSize
                    )
            )
        }
        restartControlsAutoHideTimer()
    }

    fun finishAndSaveFontSizeChange() {
        val updatedFontSize =
            currentState().fontSizeState.previewFontSize.coercedFontSize

        updateState { state ->
            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            fontSize = updatedFontSize,
                            previewFontSize = updatedFontSize
                    )
            )
        }

        saveFontSize()
    }

    fun onIncreaseFontSize() {
        updateFontSizeBy(FONT_SIZE_STEP)
    }

    fun onDecreaseFontSize() {
        updateFontSizeBy(-FONT_SIZE_STEP)
    }

    private fun updateFontSizeBy(change: Float) {
        updateState { state ->
            val updatedFontSize =
                (state.fontSizeState.fontSize + change).coerceInFontRange()

            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            fontSize = updatedFontSize,
                            previewFontSize = updatedFontSize
                    )
            )
        }
        saveFontSize()
        restartControlsAutoHideTimer()
    }

    private fun saveFontSize() {
        val fontSize = currentState().fontSizeState.fontSize

        coroutineScope.launch(Dispatchers.IO) {
            try {
                fontDataStore.saveFontSize(fontSize)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Throwable) {
                onSaveError()
            }
        }
    }

    private fun Float.coerceInFontRange(): Float {
        return coerceIn(ReaderFontSize.MIN, ReaderFontSize.MAX)
                .toInt()
                .toFloat()
    }

    private companion object {
        const val FONT_SIZE_STEP = 1f
    }
}

