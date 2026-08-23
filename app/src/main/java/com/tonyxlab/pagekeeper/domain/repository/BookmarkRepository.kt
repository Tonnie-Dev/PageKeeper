package com.tonyxlab.pagekeeper.domain.repository


import com.tonyxlab.pagekeeper.domain.model.BookWithBookmarkCount
import com.tonyxlab.pagekeeper.domain.model.Bookmark
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {

    suspend fun insertBookmark(bookmark: Bookmark)

    suspend fun updateBookmark(bookmark: Bookmark)

    suspend fun deleteBookmarkById(bookmarkId: Long)

    fun observeBookmarks(bookId: String): Flow<List<Bookmark>>

    suspend fun getBookmarkById(bookmarkId: Long): Bookmark

    suspend fun deleteAllBookmarksForBook(bookId: String)

    fun observeBooksWithBookmarks():
            Flow<List<BookWithBookmarkCount>>
}
