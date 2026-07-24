package com.tonyxlab.pagekeeper.domain.model

data class Book(
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
) {

    val progress: Float
        get() = when {

            isFinished -> 1f
            totalBlockCount <= 0 -> 0f
            else -> {

                (lastReadBlockIndex.toFloat() / totalBlockCount.toFloat()).coerceIn(0f..1f)
            }
        }
}
