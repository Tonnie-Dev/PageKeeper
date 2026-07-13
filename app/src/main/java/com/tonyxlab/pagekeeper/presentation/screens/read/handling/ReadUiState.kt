package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.ReaderBook
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState

data class ReadUiState(

    val isLoading: Boolean = false,

    val book: Book? = null,

    val document: ReaderBook? = null,

    val orientation: ReadingOrientation = ReadingOrientation.AUTO_ROTATE,

    val fontSizeState: FontSizeState = FontSizeState(),

    val immersiveMode: Boolean = true

) : UiState {

    @Stable
    data class FontSizeState(
        val fontSize: Float = ReaderFontSize.DEFAULT,
        val previewFontSize: Float = ReaderFontSize.DEFAULT,
        val showFontSizePanel: Boolean = false,
    )
}

enum class ReadingOrientation {
    AUTO_ROTATE,
    LANDSCAPE_LOCK
}

object ReaderFontSize {
    const val MIN = 10f
    const val MAX = 40f
    const val DEFAULT = 18f

    val range: ClosedFloatingPointRange<Float> = MIN..MAX
}

val Float.coercedFontSize
    get() = coerceIn(ReaderFontSize.range)

