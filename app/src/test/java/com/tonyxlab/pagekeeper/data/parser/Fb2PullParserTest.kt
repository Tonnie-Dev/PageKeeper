package com.tonyxlab.pagekeeper.data.parser

import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.tonyxlab.pagekeeper.data.model.ReaderBlock
import kotlin.io.path.createTempFile
import kotlin.io.path.writeText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Fb2PullParserTest {

    @Test
    fun `parse builds nested sections and ordered content`() {
        val book = parse(
            """
            <FictionBook xmlns="http://www.gribuser.ru/xml/fictionbook/2.0">
              <description><title-info><book-title>Ignored metadata</book-title></title-info></description>
              <body>
                <section id="chapter-1">
                  <title><p>Chapter One</p></title>
                  <p>First paragraph.</p>
                  <section id="chapter-1-1">
                    <title><p>Nested Chapter</p></title>
                    <p>Nested paragraph.</p>
                  </section>
                </section>
              </body>
            </FictionBook>
            """.trimIndent()
        ).getOrThrow()

        assertEquals(
            listOf("Chapter One", "First paragraph.", "Nested Chapter", "Nested paragraph."),
            book.content.map {
                when (it) {
                    is ReaderBlock.ChapterTitle -> it.text
                    is ReaderBlock.Paragraph -> it.text.text
                    is ReaderBlock.Quote -> it.text.text
                }
            }
        )

        val root = book.sections.single()
        assertEquals("chapter-1", root.id)
        assertEquals(1, root.level)
        assertEquals(0, root.startBlockIndex)
        val child = root.children.single()
        assertEquals("chapter-1-1", child.id)
        assertEquals(2, child.level)
        assertEquals(2, child.startBlockIndex)
    }

    @Test
    fun `parse preserves strong emphasis and quote paragraphs`() {
        val book = parse(
            """
            <FictionBook xmlns="http://www.gribuser.ru/xml/fictionbook/2.0">
              <body><section>
                <p>Plain <strong>bold</strong> and <emphasis>italic</emphasis>.</p>
                <epigraph><p>Line one</p><p>Line two</p></epigraph>
              </section></body>
            </FictionBook>
            """.trimIndent()
        ).getOrThrow()

        val paragraph = (book.content[0] as ReaderBlock.Paragraph).text
        assertEquals("Plain bold and italic.", paragraph.text)
        assertTrue(paragraph.spanStyles.any { it.item.fontWeight == FontWeight.Bold })
        assertTrue(paragraph.spanStyles.any { it.item.fontStyle == FontStyle.Italic })
        assertEquals("Line one\nLine two", (book.content[1] as ReaderBlock.Quote).text.text)
    }

    @Test
    fun `parse fails when body is missing`() {
        assertTrue(parse("<FictionBook/>").isFailure)
    }

    private fun parse(xml: String) = createTempFile(suffix = ".fb2").let { path ->
        path.writeText(xml)
        try {
            Fb2PullParser().parse(path.toFile())
        } finally {
            path.toFile().delete()
        }
    }
}
