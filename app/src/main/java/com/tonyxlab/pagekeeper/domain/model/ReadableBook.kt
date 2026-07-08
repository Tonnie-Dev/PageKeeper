package com.tonyxlab.pagekeeper.domain.model

import androidx.compose.ui.text.AnnotatedString
import com.tonyxlab.pagekeeper.data.model.BookSection
import com.tonyxlab.pagekeeper.data.model.ParsedBook
import com.tonyxlab.pagekeeper.data.model.ReaderBlock

data class ReaderBook(
    val sections: List<ReaderSection>,
    val blocks: List<ReaderContentBlock>
)

data class ReaderSection(
    val title: String?,
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

private fun BookSection.toReaderSection(): ReaderSection {
    return ReaderSection(
            title = title,
            children = children.map(BookSection::toReaderSection)
    )
}

private fun ReaderBlock.toReaderContentBlock(): ReaderContentBlock {
    return when (this) {
        is ReaderBlock.ChapterTitle -> ReaderContentBlock.ChapterTitle(
                text = text,
                level = level
        )

        is ReaderBlock.Paragraph -> ReaderContentBlock.Paragraph(text = text)
        is ReaderBlock.Quote -> ReaderContentBlock.Quote(text = text)
    }
}