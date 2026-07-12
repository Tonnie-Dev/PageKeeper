package com.tonyxlab.pagekeeper.presentation.screens.read

import androidx.lifecycle.viewModelScope
import com.tonyxlab.pagekeeper.data.parser.Fb2Parser
import com.tonyxlab.pagekeeper.domain.model.toReaderBook
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import kotlin.time.Duration.Companion.milliseconds

typealias ReadBaseViewModel = BaseViewModel<ReadUiState, ReadUiEvent, ReadActionEvent>

class ReadViewModel(
    private val fb2Parser: Fb2Parser,
    private val bookRepository: BookRepository,
    bookId: String,
) : ReadBaseViewModel(initialState = ReadUiState()) {

    private var autoHideJob: Job?  = null

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
            ReadUiEvent.DecreaseFontSize -> onDecreaseFontSize()
            ReadUiEvent.IncreaseFontSize -> onIncreaseFontSize()
            ReadUiEvent.ChangeFontSize -> showFontSlider()
            is ReadUiEvent.SetFontSize -> onChangeFontSize(event.fontSizeSp)
            ReadUiEvent.ExitReader -> exitReader()
            ReadUiEvent.ToggleImmersiveMode -> toggleImmersiveMode()
            ReadUiEvent.ToggleFavorite -> toggleFavorite()
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

    private fun showFontSlider() {
        updateState { state ->
            state.copy(fontSliderVisible = true)
        }
    }

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
    private fun toggleImmersiveMode() {
       if (currentState.immersiveMode) {
           showScreenControls()
       } else {
           hideScreenControls()
       }
    }


    private fun showScreenControls() {

        autoHideJob?.cancel()
        updateState { state -> state.copy(immersiveMode = false) }
        autoHideJob = viewModelScope.launch {

            delay(ControlAutoHideDelayMillis.milliseconds)

            updateState { state -> state.copy(immersiveMode = true) }
        }
    }

    private fun hideScreenControls() {
        autoHideJob?.cancel()
        autoHideJob = null
        updateState { state -> state.copy(immersiveMode = true) }
    }

    private fun restartControlsAutoHideTimer() {
        if (!currentState.immersiveMode) {
           showScreenControls()
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
