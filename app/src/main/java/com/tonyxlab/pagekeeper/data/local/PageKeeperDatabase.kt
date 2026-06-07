package com.tonyxlab.pagekeeper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tonyxlab.pagekeeper.data.local.dao.BookDao
import com.tonyxlab.pagekeeper.data.local.entity.BookEntity

@Database(
        entities = [BookEntity::class],
        version = 1,
        exportSchema = false
)
abstract class PageKeeperDatabase : RoomDatabase() {
    abstract val bookDao: BookDao
}
