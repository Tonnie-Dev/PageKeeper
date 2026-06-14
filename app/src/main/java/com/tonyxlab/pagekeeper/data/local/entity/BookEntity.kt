package com.tonyxlab.pagekeeper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val author: String,
    val coverPath: String?,
    val fileName: String,
    val filePath: String,
    val dateAdded: Long,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false
)
