package com.tonyxlab.pagekeeper.presentation.screens.reader

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.domain.model.ReaderBook
import com.tonyxlab.pagekeeper.presentation.core.handling.UiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.BookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiSection
import com.tonyxlab.pagekeeper.utils.AppDefaults

data class ReaderUiState(
    val isLoading: Boolean = false,

    val book: Book? = null,

    val resumeBook: Book? = null,

    val readerBook: ReaderBook? = null,

    val orientation: ReadingOrientation = ReadingOrientation.AUTO_ROTATE,

    val fontSizeState: FontSizeState = FontSizeState(),

    val controlMode: ReadingControlMode = ReadingControlMode.Immersive,

    val chapterSections: List<ChapterUiSection> = emptyList(),

    //val requestedBlockIndex: Int? = null,

    val bookmarkUiState: BookmarkUiState = BookmarkUiState(),

    val readingPosition: ReadingPosition = ReadingPosition(),


    val requestedJumpTarget: ReaderJumpTarget? = null
   // val bookmarkTarget: BookmarkTarget? = null
) : UiState {

    @Stable
    data class FontSizeState(
        val fontSize: Float = ReaderFontSize.DEFAULT,
        val previewFontSize: Float = ReaderFontSize.DEFAULT
    )

    @Stable
    data class BookmarkUiState(
        val bookmarkUiItems: List<BookmarkUiItem> = emptyList(),
        val selectedBookmarkId: Long? = null,
        val dialogInputState: DialogInputState = DialogInputState()
    ) : UiState {

        data class DialogInputState(
            val textFieldState: TextFieldState = TextFieldState(),
            val showEditBookmarkDialog: Boolean = false,
            val showDeleteBookmarkDialog: Boolean = false,
            val selectedColor: BookmarkColor = BookmarkColor.Blue,
            val blockIndex: Int? = null,
            val chapterTitle: String = "",
            val editingBookmarkId: Long? = null
        )
    }

    @Stable
    data class ReadingPosition(
        val currentBlockIndex: Int = 0,
        val textOffset: Int = 0
    )

   /* @Stable
    data class BookmarkTarget(
        val bookmarkId: Long,
        val blockIndex: Int,
        val textOffset: Int
    )*/
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

sealed interface ReaderJumpTarget {

    data class ChapterJumpTarget(
        val blockIndex: Int
    ) : ReaderJumpTarget

    data class BookmarkJumpTarget(
        val bookmarkId: Long,
        val blockIndex: Int,
        val textOffset: Int
    ) : ReaderJumpTarget
}

