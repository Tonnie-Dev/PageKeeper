package com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.mapper

import com.tonyxlab.pagekeeper.domain.model.ReaderBook
import com.tonyxlab.pagekeeper.domain.model.ReaderSection
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiSection

fun ReaderBook.toChapterSections(): List<ChapterUiSection> {
    val hasSingleAnonymousRoot =
        sections.size == 1 && sections.first().title.cleanTitle() == null

    return sections.mapIndexedNotNull { sectionIndex, section ->
        val sectionTitle = section.title.cleanTitle()

        val titledDescendants = section.children
                .flattenTitledSections()

        if (sectionTitle == null && titledDescendants.isEmpty()) {
            return@mapIndexedNotNull null
        }

        val promotedSection = titledDescendants.firstOrNull()

        val displaySectionTitle = when {
            hasSingleAnonymousRoot -> "Contents"

            sectionTitle != null -> sectionTitle

            else -> promotedSection?.title.cleanTitle()
                ?: return@mapIndexedNotNull null
        }

        val chapterSources = when {
            sectionTitle != null && titledDescendants.isNotEmpty() -> {
                titledDescendants
            }

            sectionTitle != null -> {
                listOf(section)
            }

            hasSingleAnonymousRoot -> {
                titledDescendants
            }

            else -> {
                titledDescendants
                        .drop(1)
                        .ifEmpty {
                            listOfNotNull(promotedSection)
                        }
            }
        }

        ChapterUiSection(
                id = section.id
                    ?: promotedSection?.id
                    ?: "section-$sectionIndex",
                title = displaySectionTitle,
                startBlockIndex = section.startBlockIndex,
                chapters = chapterSources
                        .distinctBy { chapter ->
                            chapter.id
                                ?: "${chapter.startBlockIndex}:${chapter.title.cleanTitle()}"
                        }
                        .sortedBy(ReaderSection::startBlockIndex)
                        .mapIndexed { chapterIndex, chapter ->
                            ChapterUiItem(
                                    id = chapter.id
                                        ?: "section-$sectionIndex-chapter-$chapterIndex",
                                    title = chapter.title.cleanTitle()
                                        ?: "Chapter ${chapterIndex + 1}",
                                    startBlockIndex = chapter.startBlockIndex
                            )
                        }
        )
    }
}

/**
 * Traverses the section hierarchy in reading order.
 *
 * Anonymous structural sections are skipped, while their titled descendants
 * are promoted into the resulting chapter list.
 */
private fun List<ReaderSection>.flattenTitledSections(): List<ReaderSection> {
    return flatMap { section ->
        buildList {
            if (section.title.cleanTitle() != null) {
                add(section)
            }

            addAll(section.children.flattenTitledSections())
        }
    }
}

private fun String?.cleanTitle(): String? {
    return this
            ?.trim()
            ?.takeIf(String::isNotEmpty)
}

