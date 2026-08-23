package com.tonyxlab.pagekeeper.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.tonyxlab.pagekeeper.data.local.database.entity.BookmarkEntity
import com.tonyxlab.pagekeeper.domain.model.BookWithBookmarkCount
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bookmark: BookmarkEntity)

    @Update
    suspend fun update(bookmark: BookmarkEntity)

    @Query("DELETE FROM bookmarks WHERE id =:bookmarkId")
    suspend fun deleteBookmarkById(bookmarkId: Long)

    @Query("SELECT * FROM bookmarks WHERE book_id = :bookId ORDER BY created_at DESC")
    fun observeBookmarks(bookId: String): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE id = :bookmarkId")
    suspend fun getBookmarkById(bookmarkId: Long): BookmarkEntity?

    @Query("DELETE FROM bookmarks WHERE book_id = :bookId")
    suspend fun deleteAllForBook(bookId: String)

    @Query(
            """
    SELECT
        book.id AS bookId,
        book.title AS title,
        book.author AS author,
        book.cover_path AS coverPath,
        COUNT(bookmark.id) AS bookmarkCount
    FROM books AS book
    INNER JOIN bookmarks AS bookmark
        ON book.id = bookmark.book_id
    GROUP BY
        book.id,
        book.title,
        book.author,
        book.cover_path
    ORDER BY book.date_added DESC
    """
    )
    fun observeBooksWithBookmarks(): Flow<List<BookWithBookmarkCount>>

}
