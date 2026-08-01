package com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.util

import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiSection

fun List<ChapterUiSection>.findCurrentChapter(
    currentBlockIndex: Int
): ChapterUiItem? {
    return flatMap(ChapterUiSection::chapters)
            .sortedBy(ChapterUiItem::startBlockIndex)
            .lastOrNull { chapter ->
                chapter.startBlockIndex <= currentBlockIndex
            }
}

fun List<ChapterUiSection>.findCurrentSectionIndex(
    currentChapterId: String?
): Int {
    if (currentChapterId == null) return 0

    val index = indexOfFirst { section ->
        section.chapters.any { chapter ->
            chapter.id == currentChapterId
        }
    }

    return index.coerceAtLeast(0)
}