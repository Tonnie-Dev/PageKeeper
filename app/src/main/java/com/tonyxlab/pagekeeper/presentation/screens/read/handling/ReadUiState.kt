package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import androidx.compose.runtime.Stable
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.ReaderBook
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.utils.AppDefaults

data class ReadUiState(

    val isLoading: Boolean = false,

    val book: Book? = null,
    val resumeBook: Book? = null,

    val document: ReaderBook? = null,

    val orientation: ReadingOrientation = ReadingOrientation.AUTO_ROTATE,

    val fontSizeState: FontSizeState = FontSizeState(),

    val controlMode: ReadingControlMode = ReadingControlMode.Immersive

) : UiState {

    @Stable
    data class FontSizeState(
        val fontSize: Float = ReaderFontSize.DEFAULT,
        val previewFontSize: Float = ReaderFontSize.DEFAULT
    )
}

enum class ReadingOrientation {
    AUTO_ROTATE,
    LANDSCAPE_LOCK
}

enum class ReadingControlMode {
    Immersive,
    DefaultToolbar,
    FontSizePanel,
}

object ReaderFontSize {
    const val MIN = AppDefaults.MIN_FONT_SIZE
    const val MAX = AppDefaults.MAX_FONT_SIZE
    const val DEFAULT = AppDefaults.DEFAULT_FONT_SIZE

    val range: ClosedFloatingPointRange<Float> = MIN..MAX
}

val Float.coercedFontSize
    get() = coerceIn(ReaderFontSize.range)

