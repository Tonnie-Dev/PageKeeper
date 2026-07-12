package com.tonyxlab.pagekeeper.data.model

import androidx.compose.ui.text.AnnotatedString




/**
 * Represents the fully parsed contents of an FB2 book.
 *
 * [sections] preserves the structural hierarchy of the FB2 document.
 * [content] preserves the sequential reading order of all renderable blocks.
 */
data class ParsedBook(
    val sections: List<BookSection>,
    val content: List<ReaderBlock>
)

/**
 * Represents a section from the FB2 document hierarchy.
 *
 * @property id
 * The optional XML ID declared by the FB2 <section> element.
 * This may later support internal links, bookmarks, footnotes,
 * and stable chapter identification.
 *
 * @property title
 * The section title, or null when the section has no title.
 *
 * @property level
 * The section's nesting depth.
 * A top-level section is typically level 1, its child is level 2, and so on.
 *
 * @property startBlockIndex
 * The index of the first [ReaderBlock] belonging to this section
 * inside [ParsedBook.content].
 *
 * @property children
 * Nested FB2 sections belonging to this section.
 */
data class BookSection(
    val id: String?,
    val title: String?,
    val level: Int,
    val startBlockIndex: Int,
    val children: List<BookSection> = emptyList()
)

/**
 * Represents a renderable piece of book content.
 *
 * The parser emits these blocks in the same order that they appear
 * in the FB2 document.
 */
sealed interface ReaderBlock {

    /**
     * A title belonging to an FB2 section.
     *
     * [level] allows the reader UI to apply different typography
     * and spacing depending on the section depth.
     */
    data class ChapterTitle(
        val text: String,
        val level: Int
    ) : ReaderBlock

    /**
     * A regular paragraph.
     *
     * [AnnotatedString] preserves inline styles such as bold and italic text.
     */
    data class Paragraph(
        val text: AnnotatedString
    ) : ReaderBlock

    /**
     * A quote or epigraph block.
     *
     * [AnnotatedString] preserves inline formatting inside the quote.
     */
    data class Quote(
        val text: AnnotatedString
    ) : ReaderBlock
}
