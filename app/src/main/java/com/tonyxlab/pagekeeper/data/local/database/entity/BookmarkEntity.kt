package com.tonyxlab.pagekeeper.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor

@Entity(
        tableName = "bookmarks",
        foreignKeys = [
            ForeignKey(
                    entity = BookEntity::class,
                    parentColumns = ["id"],
                    childColumns = ["book_id"],
                    onUpdate = ForeignKey.CASCADE,
                    onDelete = ForeignKey.CASCADE
            )
        ]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0L,
    @ColumnInfo(name = "book_id")
    val bookId: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "block_index")
    val blockIndex: Int,
    @ColumnInfo(name = "text_offset")
    val textOffset: Int,
    @ColumnInfo(name = "chapter_title")
    val chapterTitle: String,
    @ColumnInfo(name = "color")
    val color: BookmarkColor,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
)
