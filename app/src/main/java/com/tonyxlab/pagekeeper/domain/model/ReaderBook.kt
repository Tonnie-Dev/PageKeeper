package com.tonyxlab.pagekeeper.domain.model

import androidx.compose.ui.text.AnnotatedString
import com.tonyxlab.pagekeeper.data.model.BookSection
import com.tonyxlab.pagekeeper.data.model.ParsedBook
import com.tonyxlab.pagekeeper.data.model.ReaderBlock
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiSection
import kotlin.collections.flatMap
import kotlin.collections.indexOfFirst

data class ReaderBook(
    val sections: List<ReaderSection>,
    val blocks: List<ReaderContentBlock>
)

data class ReaderSection(
    val id: String?,
    val title: String?,
    val level: Int,
    val startBlockIndex: Int,
    val children: List<ReaderSection> = emptyList()
)

sealed interface ReaderContentBlock {

    data class ChapterTitle(
        val text: String,
        val level: Int
    ) : ReaderContentBlock

    data class Paragraph(
        val text: AnnotatedString
    ) : ReaderContentBlock

    data class Quote(
        val text: AnnotatedString
    ) : ReaderContentBlock
}

fun ParsedBook.toReaderBook(): ReaderBook {
    return ReaderBook(
            sections = sections.map(BookSection::toReaderSection),
            blocks = content.map(ReaderBlock::toReaderContentBlock)
    )
}

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
fun ParsedBook.toChapterSections(): List<ChapterUiSection> {
    return sections.mapIndexed { sectionIndex, section ->
        val chapterSources = section.children
                .flattenSections()
                .ifEmpty { listOf(section) }

        ChapterUiSection(
                id = section.id ?: "section-$sectionIndex",
                title = section.title.displayTitleOr("Section ${sectionIndex + 1}"),
                startBlockIndex = section.startBlockIndex,
                chapters = chapterSources.mapIndexed { chapterIndex, chapter ->
                    ChapterUiItem(
                            id = chapter.id
                                ?: "section-$sectionIndex-chapter-$chapterIndex",
                            title = chapter.title
                                    .displayTitleOr("Chapter ${chapterIndex + 1}"),
                            startBlockIndex = chapter.startBlockIndex
                    )
                }
        )
    }
}

/**
 * Collapses an arbitrarily deep FB2 section tree into the chapter level required by
 * the Chapters screen. Pre-order traversal keeps the same reading order as the source.
 */
private fun List<BookSection>.flattenSections(): List<BookSection> {
    return flatMap { section ->
        listOf(section) + section.children.flattenSections()
    }
}

private fun String?.displayTitleOr(fallback: String): String {
    return this?.trim()?.takeIf(String::isNotEmpty) ?: fallback
}

private fun BookSection.toReaderSection(): ReaderSection {
    return ReaderSection(
            id = id,
            title = title,
            level = level,
            startBlockIndex = startBlockIndex,
            children = children.map(BookSection::toReaderSection)
    )
}

private fun ReaderBlock.toReaderContentBlock(): ReaderContentBlock {
    return when (this) {
        is ReaderBlock.ChapterTitle -> {
            ReaderContentBlock.ChapterTitle(
                    text = text,
                    level = level
            )
        }

        is ReaderBlock.Paragraph -> {
            ReaderContentBlock.Paragraph(
                    text = text
            )
        }

        is ReaderBlock.Quote -> {
            ReaderContentBlock.Quote(
                    text = text
            )
        }
    }
}
