package com.tonyxlab.pagekeeper.data.local.mapper.mapper

import com.tonyxlab.pagekeeper.data.local.entity.BookEntity
import com.tonyxlab.pagekeeper.domain.model.Book

fun BookEntity.toModel() =
    Book(
            id = id,
            title = title,
            author = author,
            coverPath = coverPath,
            fileName = fileName,
            filePath = filePath,
            fileHash = fileHash,
            dateAdded = dateAdded,
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
            fileHash = fileHash,
            dateAdded = dateAdded,
            isFavorite = isFavorite,
            isFinished = isFinished
    )
