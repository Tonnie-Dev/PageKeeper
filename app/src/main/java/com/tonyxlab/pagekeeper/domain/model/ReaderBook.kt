package com.tonyxlab.pagekeeper.domain.model

import androidx.compose.ui.text.AnnotatedString
import com.tonyxlab.pagekeeper.data.model.BookSection
import com.tonyxlab.pagekeeper.data.model.ParsedBook
import com.tonyxlab.pagekeeper.data.model.ReaderBlock
import com.tonyxlab.pagekeeper.presentation.screens.chapters.model.ChapterUiItem
import com.tonyxlab.pagekeeper.presentation.screens.chapters.model.ChapterUiSection
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
        val sectionTitle =
            section.title
                    ?.takeIf(String::isNotBlank)
                ?: "Section ${sectionIndex + 1}"

        val chapters =
            if (section.children.isNotEmpty()) {
                section.children.mapIndexed { chapterIndex, chapter ->
                    ChapterUiItem(
                            id = chapter.id
                                ?: "section-$sectionIndex-chapter-$chapterIndex",
                            title = chapter.title
                                    ?.takeIf(String::isNotBlank)
                                ?: "Chapter ${chapterIndex + 1}",
                            startBlockIndex = chapter.startBlockIndex
                    )
                }
            } else {
                /*
                 * A top-level section with no children may itself represent
                 * a chapter. We expose it as one selectable chapter.
                 */
                listOf(
                        ChapterUiItem(
                                id = section.id ?: "section-$sectionIndex-chapter-0",
                                title = section.title
                                        ?.takeIf(String::isNotBlank)
                                    ?: "Chapter 1",
                                startBlockIndex = section.startBlockIndex
                        )
                )
            }

        ChapterUiSection(
                id = section.id ?: "section-$sectionIndex",
                title = sectionTitle,
                startBlockIndex = section.startBlockIndex,
                chapters = chapters
        )
    }
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