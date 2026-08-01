package com.tonyxlab.pagekeeper.data.parser.mapper

import com.tonyxlab.pagekeeper.data.model.BookSection
import com.tonyxlab.pagekeeper.data.model.ParsedBook
import com.tonyxlab.pagekeeper.data.model.ReaderBlock
import com.tonyxlab.pagekeeper.domain.model.ReaderBook
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.domain.model.ReaderSection

fun ParsedBook.toReaderBook(): ReaderBook {
    return ReaderBook(
            sections = sections.map(BookSection::toReaderSection),
            blocks = content.map(ReaderBlock::toReaderContentBlock)
    )
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