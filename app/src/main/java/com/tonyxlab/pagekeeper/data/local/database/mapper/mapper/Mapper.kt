package com.tonyxlab.pagekeeper.data.local.database.mapper.mapper

import com.tonyxlab.pagekeeper.data.local.database.entity.BookEntity
import com.tonyxlab.pagekeeper.domain.model.Book

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
            isFavorite = isFavorite,
            isFinished = isFinished
    )
