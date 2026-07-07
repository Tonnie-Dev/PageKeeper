package com.tonyxlab.pagekeeper.data.model

import androidx.compose.ui.text.AnnotatedString

data class ParsedBook(
        val sections:List<BookSection>,
        val content:List<ReaderBlock>

)

data class BookSection(
    val title: String?,
    val children: List<BookSection> = emptyList()
)


sealed interface ReaderBlock{

    data class ChapterTitle(val text:String, val level: Int): ReaderBlock
    data class Paragraph(val text: AnnotatedString): ReaderBlock
    data class Quote(val text: AnnotatedString): ReaderBlock

}
