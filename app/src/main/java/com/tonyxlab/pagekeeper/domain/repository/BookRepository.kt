package com.tonyxlab.pagekeeper.domain.repository

import com.tonyxlab.pagekeeper.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {
    fun observeBooks(): Flow<List<Book>>

    suspend fun existsByHash(fileHash: String): Boolean

    suspend fun insertBook(book: Book)

    suspend fun updateFavorite(bookId: String, isFavorite: Boolean)

    suspend fun updateFinished(bookId: String, isFinished: Boolean)

    suspend fun deleteBookById(id: String)
}
