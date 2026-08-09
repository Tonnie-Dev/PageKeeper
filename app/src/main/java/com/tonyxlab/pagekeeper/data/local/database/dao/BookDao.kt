package com.tonyxlab.pagekeeper.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyxlab.pagekeeper.data.local.database.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY date_added DESC")
    fun observeBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE id = :id)")
    suspend fun existsByHash(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: BookEntity)

    @Query("UPDATE books SET is_favorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavorite(bookId: String, isFavorite: Boolean)

    @Query("UPDATE books SET is_finished = :isFinished WHERE id = :bookId")
    suspend fun updateFinished(bookId: String, isFinished: Boolean)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: String)

    @Query(
            """
    SELECT * FROM books
    WHERE title LIKE '%' || :query || '%' COLLATE NOCASE
       OR author LIKE '%' || :query || '%' COLLATE NOCASE
    ORDER BY date_added DESC
    """
    )
    fun searchBooks(query: String): Flow<List<BookEntity>>

    @Query(
            """
        UPDATE books
        SET last_read_block_index = :lastReadBlockIndex,
total_block_count = :totalBlockCount
WHERE id = :bookId
    """
    )
    suspend fun updateReadingProgress(bookId: String, lastReadBlockIndex: Int, totalBlockCount: Int)

    @Query(
            """
  UPDATE books
  SET last_read_at = :lastOpenedAt
  WHERE id =:bookId
   """
    )
    suspend fun updateLastOpenedAt(bookId: String, lastOpenedAt: Long)

    @Query(
            """
SELECT * FROM books
WHERE last_read_at   IS NOT NULL
AND is_finished = 0
ORDER BY last_read_at DESC
LIMIT 1
 """
    )
    fun observeResumeBook(): Flow<BookEntity?>
}
