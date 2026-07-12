package com.tonyxlab.pagekeeper.presentation.screens.read.handling

import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.ReaderBook
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState

data class ReadUiState(

    val isLoading: Boolean = false,

    val book: Book? = null,

    val document: ReaderBook? = null,

    val controlsVisible: Boolean = false,

    val fontSliderVisible: Boolean = false,

    val orientation: ReadingOrientation =
        ReadingOrientation.AUTO_ROTATE,

    val fontSizeSp: Float = 18f,

    val immersiveMode: Boolean = true
) : UiState

enum class ReadingOrientation {
    AUTO_ROTATE,
    LANDSCAPE_LOCK
}