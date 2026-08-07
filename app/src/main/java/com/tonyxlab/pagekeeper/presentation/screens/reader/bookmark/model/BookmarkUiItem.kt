package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model

import androidx.compose.ui.graphics.Color
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor

data class BookmarkUiItem(
    val id: Long,
    val blockIndex: Int,
    val blockOffset: Int,
    val text: String,
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
