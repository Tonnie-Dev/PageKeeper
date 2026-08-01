package com.tonyxlab.pagekeeper.domain.model

import com.tonyxlab.pagekeeper.data.model.BookSection
import com.tonyxlab.pagekeeper.data.model.ParsedBook
import kotlin.test.Test
import kotlin.test.assertEquals

class ReaderBookTest {

    @Test
    fun `toChapterSections creates a two-level hierarchy for a collection of books`() {
        val parsedBook = parsedBook(
            section(
                id = "tom-sawyer",
                title = "The Adventures of Tom Sawyer",
                startBlockIndex = 0,
                children = listOf(
                    section("tom-1", "Chapter I", 1, level = 2),
                    section("tom-2", "Chapter II", 10, level = 2)
                )
            ),
            section(
                id = "prince-pauper",
                title = "The Prince and the Pauper",
                startBlockIndex = 20,
                children = listOf(
                    section("prince-1", "Chapter I", 21, level = 2),
                    section("prince-2", "Chapter II", 30, level = 2)
                )
            )
        )

        val result = parsedBook.toChapterSections()

        assertEquals(
            listOf("The Adventures of Tom Sawyer", "The Prince and the Pauper"),
            result.map { it.title }
        )
        assertEquals(listOf("Chapter I", "Chapter II"), result[0].chapters.map { it.title })
        assertEquals(listOf("Chapter I", "Chapter II"), result[1].chapters.map { it.title })
    }

    @Test
    fun `toChapterSections flattens deeper nesting in reading order`() {
        val parsedBook = parsedBook(
            section(
                id = "book",
                title = "Book",
                startBlockIndex = 0,
                children = listOf(
                    section(
                        id = "part",
                        title = "Part I",
                        startBlockIndex = 1,
                        level = 2,
                        children = listOf(
                            section("chapter-1", "Chapter I", 2, level = 3),
                            section("chapter-2", "Chapter II", 12, level = 3)
                        )
                    ),
                    section("epilogue", "Epilogue", 20, level = 2)
                )
            )
        )

        val chapters = parsedBook.toChapterSections().single().chapters

        assertEquals(listOf("Part I", "Chapter I", "Chapter II", "Epilogue"), chapters.map { it.title })
        assertEquals(listOf(1, 2, 12, 20), chapters.map { it.startBlockIndex })
    }

    @Test
    fun `toChapterSections generates labels for untitled sections and chapters`() {
        val parsedBook = parsedBook(
            section(
                id = null,
                title = "  ",
                startBlockIndex = 0,
                children = listOf(
                    section(null, null, 1, level = 2),
                    section(null, "", 5, level = 2)
                )
            )
        )

        val result = parsedBook.toChapterSections().single()

        assertEquals("Section 1", result.title)
        assertEquals(listOf("Chapter 1", "Chapter 2"), result.chapters.map { it.title })
        assertEquals(
            listOf("section-0-chapter-0", "section-0-chapter-1"),
            result.chapters.map { it.id }
        )
    }

    @Test
    fun `toChapterSections exposes a leaf top-level section as a selectable chapter`() {
        val parsedBook = parsedBook(section("chapter-1", "Chapter I", 7))

        val result = parsedBook.toChapterSections().single()

        assertEquals("Chapter I", result.title)
        assertEquals("chapter-1", result.chapters.single().id)
        assertEquals("Chapter I", result.chapters.single().title)
        assertEquals(7, result.chapters.single().startBlockIndex)
    }

    private fun parsedBook(vararg sections: BookSection) = ParsedBook(
        sections = sections.toList(),
        content = emptyList()
    )

    private fun section(
        id: String?,
        title: String?,
        startBlockIndex: Int,
        level: Int = 1,
        children: List<BookSection> = emptyList()
    ) = BookSection(
        id = id,
        title = title,
        level = level,
        startBlockIndex = startBlockIndex,
        children = children
    )
}
