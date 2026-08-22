package com.tonyxlab.pagekeeper.presentation.screens.bookmarks.model

import com.tonyxlab.pagekeeper.domain.model.Book

data class GlobalBookmarkUiItem(
    val bookId: String,
    val title: String,
    val author: String,
    val coverPath: String?,
    val bookmarkCount: Int,
)

fun Book.toGlobalBookmarkUiItem(
    bookmarkCount: Int,
): GlobalBookmarkUiItem {
    return GlobalBookmarkUiItem(
            bookId = id,
            title = title,
            author = author,
            coverPath = coverPath,
            bookmarkCount = bookmarkCount,
    )
}