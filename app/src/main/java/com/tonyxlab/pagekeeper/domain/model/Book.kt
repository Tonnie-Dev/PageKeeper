package com.tonyxlab.pagekeeper.domain.model

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val coverPath: String?,
    val fileName: String,
    val filePath: String,
    val fileHash: String,
    val dateAdded: Long,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false
)