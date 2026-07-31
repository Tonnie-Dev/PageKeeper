package com.tonyxlab.pagekeeper.presentation.screens.chapters.model

data class ChapterUiSection(
    val id: String,
    val title: String,
    val startBlockIndex: Int,
    val chapters: List<ChapterUiItem>
)

data class ChapterUiItem(
    val id: String,
    val title: String,
    val startBlockIndex: Int
)