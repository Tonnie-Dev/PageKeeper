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

