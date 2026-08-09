package com.tonyxlab.pagekeeper.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tonyxlab.pagekeeper.data.local.database.dao.BookDao
import com.tonyxlab.pagekeeper.data.local.database.dao.BookmarkDao
import com.tonyxlab.pagekeeper.data.local.database.entity.BookEntity
import com.tonyxlab.pagekeeper.data.local.database.entity.BookmarkEntity

@Database(
        entities = [BookEntity::class, BookmarkEntity::class],
        version = 1,
        exportSchema = false
)
abstract class PageKeeperDatabase : RoomDatabase() {
    abstract val bookDao: BookDao
    abstract val bookmarkDao: BookmarkDao
}
