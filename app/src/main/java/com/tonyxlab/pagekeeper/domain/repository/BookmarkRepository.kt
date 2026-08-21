package com.tonyxlab.pagekeeper.domain.repository

import com.tonyxlab.pagekeeper.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    suspend fun insertBookmark(bookmark: Bookmark)

    suspend fun updateBookmark(bookmark: Bookmark)

    suspend fun deleteBookmark(bookmark: Bookmark)

    fun observeBookmarks(bookId: String): Flow<List<Bookmark>>

    suspend fun getBookmarkById(bookmarkId: Long): Bookmark

    suspend fun deleteAllBookmarksForBook(bookId: String)
}
