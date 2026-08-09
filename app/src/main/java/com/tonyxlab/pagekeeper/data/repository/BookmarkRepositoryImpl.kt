package com.tonyxlab.pagekeeper.data.repository

import com.tonyxlab.pagekeeper.data.local.database.dao.BookmarkDao
import com.tonyxlab.pagekeeper.data.local.database.entity.BookmarkEntity
import com.tonyxlab.pagekeeper.data.local.database.mapper.mapper.toEntity
import com.tonyxlab.pagekeeper.data.local.database.mapper.mapper.toModel
import com.tonyxlab.pagekeeper.domain.model.Bookmark
import com.tonyxlab.pagekeeper.domain.repository.BookmarkRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class BookmarkRepositoryImpl(
    private val bookmarkDao: BookmarkDao
) : BookmarkRepository {

    override suspend fun insertBookmark(bookmark: Bookmark) =
        bookmarkDao.insert(bookmark.toEntity())

    override suspend fun updateBookmark(bookmark: Bookmark) =
        bookmarkDao.update(bookmark.toEntity())

    override suspend fun deleteBookmark(bookmark: Bookmark) =
        bookmarkDao.delete(bookmark.toEntity())

    override fun observeBookmarks(bookId: String): Flow<List<Bookmark>> =
        bookmarkDao.observeBookmarks(bookId)
                .map { bookmarks ->
                    bookmarks.map(BookmarkEntity::toModel)
                }

    override suspend fun deleteAllBookmarksForBook(bookId: String) =
        bookmarkDao.deleteAllForBook(bookId)
}
