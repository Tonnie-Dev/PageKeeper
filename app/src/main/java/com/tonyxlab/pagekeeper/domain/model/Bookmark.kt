package com.tonyxlab.pagekeeper.domain.model

data class Bookmark(
    val id: Long = 0L,
    val bookId: String,
    val text: String,
    val blockIndex: Int,
    val textOffset: Int,
    val chapterTitle: String,
    val color: BookmarkColor,
    val createdAt: Long,
)

enum class BookmarkColor {
    Yellow,
    Blue,
    Green,
    Orange,
    Purple
}