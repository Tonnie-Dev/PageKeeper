package com.tonyxlab.pagekeeper.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "cover_path")
    val coverPath: String?,
    @ColumnInfo(name = "file_name")
    val fileName: String,
    @ColumnInfo(name = "file_path")
    val filePath: String,
    @ColumnInfo(name = "date_added")
    val dateAdded: Long,
    @ColumnInfo(name = "last_read_block_index")
    val lastReadBlockIndex: Int = 0,
    @ColumnInfo(name = "total_block_count")
    val totalBlockCount: Int = 0,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean = false,
    @ColumnInfo(name = "last_read_at")
    val lastReadAt:Long? = null,
    @ColumnInfo(name = "is_finished")
    val isFinished: Boolean = false
)
