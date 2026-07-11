package com.tonyxlab.pagekeeper.presentation.screens.read

import com.tonyxlab.pagekeeper.data.parser.Fb2Parser
import com.tonyxlab.pagekeeper.domain.model.toReaderBook
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import com.tonyxlab.pagekeeper.presentation.core.BaseViewModel
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadingOrientation
import kotlinx.coroutines.Dispatchers
import java.io.File

typealias ReadBaseViewModel = BaseViewModel<ReadUiState, ReadUiEvent, ReadActionEvent>

class ReadViewModel(
    private val fb2Parser: Fb2Parser,
    private val bookRepository: BookRepository
) : ReadBaseViewModel(initialState = ReadUiState()) {

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

    private companion object {
        const val MinFontSizeSp = 16f
        const val MaxFontSizeSp = 24f
        const val FontSizeStep = 1f
    }
}
