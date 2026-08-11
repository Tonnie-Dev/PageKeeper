package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model

import androidx.compose.ui.graphics.Color
import com.tonyxlab.pagekeeper.domain.model.Bookmark
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor

data class BookmarkUiItem(
    val id: Long,
    val text: String,
    val blockIndex: Int,
    val textOffset:Int,
    val chapterTitle: String,
    val color: BookmarkColor,
    val createdAt: Long,
)

fun BookmarkColor.toColor(): Color =
    when (this) {
        BookmarkColor.Yellow -> Color(0xFFD4DF00)
        BookmarkColor.Blue -> Color(0xFF1565D8)
        BookmarkColor.Green -> Color(0xFF00C875)
        BookmarkColor.Orange -> Color(0xFFE53916)
        BookmarkColor.Purple -> Color(0xFFD000B8)
    }

fun Bookmark.toBookmarkUiItem() =
    BookmarkUiItem(
            id = id,
            text = text,
            blockIndex = blockIndex,
            textOffset = textOffset,
            chapterTitle = chapterTitle,
            color = color,
            createdAt = createdAt
    )
