package com.tonyxlab.pagekeeper.data.local.database.mapper.mapper

import com.tonyxlab.pagekeeper.data.local.database.entity.BookEntity
import com.tonyxlab.pagekeeper.data.local.database.entity.BookmarkEntity
import com.tonyxlab.pagekeeper.domain.model.Book
import com.tonyxlab.pagekeeper.domain.model.Bookmark

fun BookEntity.toModel() =
    Book(
            id = id,
            title = title,
            author = author,
            coverPath = coverPath,
            fileName = fileName,
            filePath = filePath,
            dateAdded = dateAdded,
            lastReadBlockIndex = lastReadBlockIndex,
            totalBlockCount = totalBlockCount,
            lastReadAt = lastReadAt,
            isFavorite = isFavorite,
            isFinished = isFinished
    )

fun Book.toEntity() =
    BookEntity(
            id = id,
            title = title,
            author = author,
            coverPath = coverPath,
            fileName = fileName,
            filePath = filePath,
            dateAdded = dateAdded,
            lastReadBlockIndex = lastReadBlockIndex,
            totalBlockCount = totalBlockCount,
            lastReadAt = lastReadAt,
            isFavorite = isFavorite,
            isFinished = isFinished
    )

fun BookmarkEntity.toModel() =
    Bookmark(
            id = id,
            bookId = bookId,
            blockIndex = blockIndex,
            text = text,
            chapterTitle = chapterTitle,
            color = color,
            createdAt = createdAt
    )

fun Bookmark.toEntity() =
    BookmarkEntity(
            id = id,
            bookId = bookId,
            blockIndex = blockIndex,
            text = text,
            chapterTitle = chapterTitle,
            color = color,
            createdAt = createdAt
    )
