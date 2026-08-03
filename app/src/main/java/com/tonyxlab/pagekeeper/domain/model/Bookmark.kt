package com.tonyxlab.pagekeeper.domain.model

data class Bookmark(

    val id: Long = 0L,

    val bookHash: String,

    val blockIndex: Int,

    val blockOffset: Int = 0,

    val title: String,

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