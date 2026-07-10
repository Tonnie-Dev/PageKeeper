package com.tonyxlab.pagekeeper.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tonyxlab.pagekeeper.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    fun observeBooks(): Flow<List<BookEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    suspend fun getBookById(id: String): BookEntity?

    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE id = :id)")
    suspend fun existsByHash(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBook(book: BookEntity)

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavorite(bookId: String, isFavorite: Boolean)

    @Query("UPDATE books SET isFinished = :isFinished WHERE id = :bookId")
    suspend fun updateFinished(bookId: String, isFinished: Boolean)

    @Query("DELETE FROM books WHERE id = :id")
    suspend fun deleteBookById(id: String)

    @Query(
            """
    SELECT * FROM books
    WHERE title LIKE '%' || :query || '%' COLLATE NOCASE
       OR author LIKE '%' || :query || '%' COLLATE NOCASE
    ORDER BY dateAdded DESC
    """
    )
    fun searchBooks(query: String): Flow<List<BookEntity>>
}
