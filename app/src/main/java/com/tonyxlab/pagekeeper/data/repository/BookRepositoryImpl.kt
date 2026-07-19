package com.tonyxlab.pagekeeper.data.repository

import com.tonyxlab.pagekeeper.data.local.database.dao.BookDao
import com.tonyxlab.pagekeeper.data.local.database.entity.BookEntity
import com.tonyxlab.pagekeeper.data.local.database.mapper.mapper.toEntity
import com.tonyxlab.pagekeeper.data.local.database.mapper.mapper.toModel
import com.tonyxlab.pagekeeper.domain.exception.ItemNotFoundException
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class BookRepositoryImpl(
    private val bookDao: BookDao
) : BookRepository {

    override fun observeBooks(): Flow<List<Book>> {
        return bookDao.observeBooks().map {books ->
            books.map(BookEntity::toModel)

        }
    }

    override suspend fun getBookById(id: String): Book {

        return bookDao.getBookById(id)?.toModel() ?: throw ItemNotFoundException(id)
    }

    override suspend fun existsByHash(fileHash: String): Boolean = bookDao.existsByHash(fileHash)

    override suspend fun insertBook(book: Book) = bookDao.insertBook(book.toEntity())

    override suspend fun updateFavorite(bookId: String, isFavorite: Boolean) {
        bookDao.updateFavorite(bookId, isFavorite)
    }

    override suspend fun updateFinished(bookId: String, isFinished: Boolean) {
        bookDao.updateFinished(bookId, isFinished)
    }

    override suspend fun deleteBookById(id: String) = bookDao.deleteBookById(id)

    override fun searchBooks(query: String): Flow<List<Book>>  =
        bookDao.searchBooks(query).map { books ->
            books.map (BookEntity::toModel)
        }

}
