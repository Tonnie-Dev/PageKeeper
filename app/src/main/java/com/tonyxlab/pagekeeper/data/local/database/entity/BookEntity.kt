package com.tonyxlab.pagekeeper.data.local.database.entity

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
    val lastReadBlockIndex: Int = 0,
    val totalBlockCount: Int = 0,
    val isFavorite: Boolean = false,
    val isFinished: Boolean = false
)
