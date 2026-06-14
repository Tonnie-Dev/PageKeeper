package com.tonyxlab.pagekeeper.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tonyxlab.pagekeeper.data.local.entity.BookEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Query("SELECT * FROM books ORDER BY dateAdded DESC")
    fun observeBooks(): Flow<List<BookEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE id = :fileHash)")
    suspend fun existsByHash(fileHash: String): Boolean

    @Insert
    suspend fun insertBook(book: BookEntity)

    @Query("UPDATE books SET isFavorite = :isFavorite WHERE id = :bookId")
    suspend fun updateFavorite(bookId: String, isFavorite: Boolean)

    @Query("UPDATE books SET isFinished = :isFinished WHERE id = :bookId")
    suspend fun updateFinished(bookId: String, isFinished: Boolean)

    @Delete
    suspend fun deleteBook(book: BookEntity)
}
