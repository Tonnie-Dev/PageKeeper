package com.tonyxlab.pagekeeper.data.parser

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.tonyxlab.pagekeeper.data.model.BookSection
import com.tonyxlab.pagekeeper.data.model.ParsedBook
import com.tonyxlab.pagekeeper.data.model.ReaderBlock
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.File
import java.io.FileInputStream

class Fb2PullParser : Fb2Parser {

    override fun parse(file: File): Result<ParsedBook> = runCatching {
        FileInputStream(file).use { input ->
            val parser = XmlPullParserFactory.newInstance().apply {
                isNamespaceAware = true
            }.newPullParser()

            parser.setInput(input, null)
            parser.moveToBodyOrThrow()
            parseBody(parser)
        }
    }

    private fun parseBody(parser: XmlPullParser): ParsedBook {
        val sections = mutableListOf<BookSection>()
        val content = mutableListOf<ReaderBlock>()

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    SECTION_TAG -> sections += parseSection(parser, ROOT_SECTION_LEVEL, content)
                    else -> parser.skipTag()
                }

                XmlPullParser.END_TAG -> if (parser.name == BODY_TAG) {
                    return ParsedBook(sections = sections, content = content)
                }
            }
        }

        throw XmlPullParserException(MISSING_BODY_ERROR)
    }

    private fun parseSection(
        parser: XmlPullParser,
        level: Int,
        content: MutableList<ReaderBlock>
    ): BookSection {
        val id = parser.attributeValue(ID_ATTRIBUTE)
        val startBlockIndex = content.size
        var title: String? = null
        val children = mutableListOf<BookSection>()

        while (parser.next() != XmlPullParser.END_DOCUMENT) {
            when (parser.eventType) {
                XmlPullParser.START_TAG -> when (parser.name) {
                    TITLE_TAG -> {
                        val parsedTitle = parser.readPlainTextUntilEnd(TITLE_TAG)
                            .normalizeText()
                            .ifBlank { null }
                        if (parsedTitle != null) {
                            title = parsedTitle
                            content += ReaderBlock.ChapterTitle(parsedTitle, level)
                        }
                    }

                    PARAGRAPH_TAG -> parser.readStyledTextUntilEnd(PARAGRAPH_TAG)
                        .takeIf { it.text.isNotBlank() }
                        ?.let { content += ReaderBlock.Paragraph(it) }

                    CITE_TAG, EPIGRAPH_TAG -> parser.readStyledTextUntilEnd(parser.name)
                        .takeIf { it.text.isNotBlank() }
                        ?.let { content += ReaderBlock.Quote(it) }

                    SECTION_TAG -> children += parseSection(parser, level + 1, content)
                    else -> parser.skipTag()
                }

                XmlPullParser.END_TAG -> if (parser.name == SECTION_TAG) {
                    return BookSection(id, title, level, startBlockIndex, children)
                }
            }
        }

        throw XmlPullParserException(UNCLOSED_SECTION_ERROR)
    }

    private fun XmlPullParser.moveToBodyOrThrow() {
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && name == BODY_TAG) return
            next()
        }
        throw XmlPullParserException(MISSING_BODY_ERROR)
    }

    private fun XmlPullParser.attributeValue(attributeName: String): String? {
        for (index in 0 until attributeCount) {
            if (getAttributeName(index) == attributeName) return getAttributeValue(index)
        }
        return null
    }

    private fun XmlPullParser.readPlainTextUntilEnd(endTag: String): String {
        val result = StringBuilder()
        while (next() != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.TEXT -> result.append(text)
                XmlPullParser.START_TAG -> result.append(readPlainTextUntilEnd(name))
                XmlPullParser.END_TAG -> if (name == endTag) return result.toString()
            }
        }
        return result.toString()
    }

    private fun XmlPullParser.readStyledTextUntilEnd(endTag: String): AnnotatedString {
        val builder = AnnotatedString.Builder()
        appendStyledTextUntilEnd(builder, endTag)
        return builder.toAnnotatedString().trimWhitespace()
    }

    private fun XmlPullParser.appendStyledTextUntilEnd(
        builder: AnnotatedString.Builder,
        endTag: String
    ) {
        while (next() != XmlPullParser.END_DOCUMENT) {
            when (eventType) {
                XmlPullParser.TEXT -> builder.append(text.replace(WHITESPACE_REGEX, " "))
                XmlPullParser.START_TAG -> when (name) {
                    STRONG_TAG, EMPHASIS_TAG -> {
                        builder.pushStyle(name.toSpanStyle())
                        appendStyledTextUntilEnd(builder, name)
                        builder.pop()
                    }

                    PARAGRAPH_TAG -> {
                        if (builder.length > 0) builder.append("\n")
                        appendStyledTextUntilEnd(builder, PARAGRAPH_TAG)
                    }

                    else -> appendStyledTextUntilEnd(builder, name)
                }

                XmlPullParser.END_TAG -> if (name == endTag) return
            }
        }
    }

    private fun XmlPullParser.skipTag() {
        if (eventType != XmlPullParser.START_TAG) return
        var depth = 1
        while (depth > 0) {
            when (next()) {
                XmlPullParser.START_TAG -> depth++
                XmlPullParser.END_TAG -> depth--
                XmlPullParser.END_DOCUMENT -> return
            }
        }
    }

    private fun String.toSpanStyle(): SpanStyle = when (this) {
        STRONG_TAG -> SpanStyle(fontWeight = FontWeight.Bold)
        EMPHASIS_TAG -> SpanStyle(fontStyle = FontStyle.Italic)
        else -> SpanStyle()
    }

    private fun String.normalizeText(): String = trim()
        .replace(WHITESPACE_REGEX, " ")
        .replace(NEWLINE_SPACING_REGEX, "\n")

    private fun AnnotatedString.trimWhitespace(): AnnotatedString {
        val start = text.indexOfFirst { !it.isWhitespace() }
        if (start == -1) return AnnotatedString("")
        val end = text.indexOfLast { !it.isWhitespace() }
        return subSequence(start, end + 1)
    }

    private companion object {
        const val BODY_TAG = "body"
        const val SECTION_TAG = "section"
        const val TITLE_TAG = "title"
        const val PARAGRAPH_TAG = "p"
        const val CITE_TAG = "cite"
        const val EPIGRAPH_TAG = "epigraph"
        const val STRONG_TAG = "strong"
        const val EMPHASIS_TAG = "emphasis"
        const val ID_ATTRIBUTE = "id"
        const val ROOT_SECTION_LEVEL = 1
        const val MISSING_BODY_ERROR = "Failed to read the book. The file may be empty or corrupted."
        const val UNCLOSED_SECTION_ERROR = "Failed to read the book. A section is not closed."

        val WHITESPACE_REGEX = Regex("[ \\t\\x0B\\f\\r]+")
        val NEWLINE_SPACING_REGEX = Regex(" *\\n *")
    }
}
