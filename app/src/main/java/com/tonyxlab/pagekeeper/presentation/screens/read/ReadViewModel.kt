package com.tonyxlab.pagekeeper.presentation.screens.read

import com.tonyxlab.pagekeeper.data.parser.Fb2Parser
import com.tonyxlab.pagekeeper.domain.model.toReaderBook
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingControlMode
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.coercedFontSize
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

typealias ReadBaseViewModel = BaseViewModel<ReadUiState, ReadUiEvent, ReadActionEvent>

class ReadViewModel(
    private val fb2Parser: Fb2Parser,
    private val bookRepository: BookRepository,
    bookId: String,
) : ReadBaseViewModel(initialState = ReadUiState()) {

    private var autoHideJob: Job? = null

    init {
        loadBook(bookId)
    }

    fun loadBook(bookId: String) {
        if (currentState.book?.id == bookId && (currentState.document != null || currentState.isLoading)) {
            return
        }

        launchCatching(
                context = Dispatchers.IO,
                onStart = {
                    updateState {
                        it.copy(
                                book = null,
                                document = null,
                                isLoading = true
                        )
                    }
                },
                onError = { error -> onBookLoadFailed(error) },
                onCompletion = { updateState { it.copy(isLoading = false) } }
        ) {
            val book = bookRepository.getBookById(bookId)
            updateState { it.copy(book = book) }

            fb2Parser.parse(File(book.filePath))
                    .fold(
                            onSuccess = { parsedBook ->
                                updateState { it.copy(document = parsedBook.toReaderBook()) }
                            },
                            onFailure = ::onBookLoadFailed
                    )
        }
    }

    override fun onEvent(event: ReadUiEvent) {
        when (event) {
            ReadUiEvent.ToggleAutoRotate -> onToggleAutoRotate()
            ReadUiEvent.IncreaseFontSize -> onIncreaseFontSize()
            ReadUiEvent.DecreaseFontSize -> onDecreaseFontSize()
            ReadUiEvent.FontSizeClicked -> showFontSizePanel()
            ReadUiEvent.ExitReader -> exitReader()
            ReadUiEvent.ReadingAreaClicked -> onReadingAreaClicked()
            ReadUiEvent.ToggleFavorite -> toggleFavorite()
            is ReadUiEvent.PreviewFontSizeChange -> previewFontSize(event.fontSize)
            is ReadUiEvent.FontSizeChangeFinished -> finishAndSaveFontSizeChange(event.fontSize)
        }
    }

    private fun onBookLoadFailed(error: Throwable) {
        updateState { it.copy(document = null) }
        sendActionEvent(
                ReadActionEvent.ShowToast(
                        error.message ?: "Unable to read this book."
                )
        )
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

    private fun showFontSizePanel() {
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

    private fun previewFontSize(fontSize: Float) {
        updateState { state ->
            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            previewFontSize = fontSize.coercedFontSize
                    )
            )

        }
        restartControlsAutoHideTimer()
    }

    private fun finishAndSaveFontSizeChange(fontSize: Float) {

        updateState { state ->
            state.copy(
                    fontSizeState = state.fontSizeState.copy(
                            fontSize = fontSize.coercedFontSize,
                            previewFontSize = fontSize.coercedFontSize,

                            )
            )

        }

        // TODO: Add Prefs 
    }

    private fun onDecreaseFontSize() {
        updateState { state ->
            state.copy(fontSizeState = state.fontSizeState.copy(fontSize = (state.fontSizeState.fontSize - FontSizeStep).coerceInFontRange()))
        }
        restartControlsAutoHideTimer()
    }

    private fun onIncreaseFontSize() {
        updateState { state ->
            state.copy(fontSizeState = state.fontSizeState.copy(fontSize = (state.fontSizeState.fontSize + FontSizeStep).coerceInFontRange()))
        }
        restartControlsAutoHideTimer()
    }

    private fun onChangeFontSize(fontSizeSp: Float) {
        updateState { state ->
            state.copy(fontSizeState = state.fontSizeState.copy(fontSize = fontSizeSp.coerceInFontRange()))
        }
    }

    private fun Float.coerceInFontRange(): Float {
        return coerceIn(MinFontSizeSp, MaxFontSizeSp)
                .toInt()
                .toFloat()
    }

    private fun toggleFavorite() {
        val book = currentState.book ?: return
        val updatedFavorite = !book.isFavorite

        updateState { state ->
            state.copy(book = book.copy(isFavorite = updatedFavorite))
        }

        launchCatching(
                context = Dispatchers.IO,
                onError = {
                    updateState { state ->
                        state.copy(book = state.book?.copy(isFavorite = book.isFavorite))
                    }
                    sendActionEvent(ReadActionEvent.ShowToast("Unable to update favorite status."))
                }
        ) {
            bookRepository.updateFavorite(book.id, updatedFavorite)
        }
    }

    private fun onReadingAreaClicked() {
       when(currentState.controlMode) {
           ReadingControlMode.Immersive -> {
               showScreenControls()
           }
           else -> {
               hideScreenControls()
           }
       }
    }

    private fun showScreenControls() {

        updateState { state -> state.copy(controlMode = ReadingControlMode.DefaultToolbar) }
        restartControlsAutoHideTimer()
    }

    private fun hideScreenControls() {
        autoHideJob?.cancel()
        autoHideJob = null
        updateState { state -> state.copy(controlMode = ReadingControlMode.Immersive) }
    }

    private fun restartControlsAutoHideTimer() {
        autoHideJob?.cancel()
        if (currentState.controlMode == ReadingControlMode.Immersive) {
            autoHideJob = null
            return
        }

        autoHideJob = launch {

            delay(ControlAutoHideDelayMillis.milliseconds)

            updateState { state -> state.copy(controlMode = ReadingControlMode.Immersive) }
        }
    }

    private fun exitReader() {
        sendActionEvent(ReadActionEvent.ExitReader)
    }

    private companion object {
        const val MinFontSizeSp = 16f
        const val MaxFontSizeSp = 24f
        const val FontSizeStep = 1f
        const val ControlAutoHideDelayMillis = 3_000L
    }
}
