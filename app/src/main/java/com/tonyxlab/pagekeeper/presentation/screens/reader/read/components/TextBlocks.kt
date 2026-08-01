package com.tonyxlab.pagekeeper.presentation.screens.reader.read.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.presentation.theme.ChapterTitle
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.ParagraphItalic
import com.tonyxlab.pagekeeper.presentation.theme.TextModalPrimary
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun ChapterTitleBlock(
    block: ReaderContentBlock.ChapterTitle,
    fontSizeSp: Float,
    modifier: Modifier = Modifier
) {

    val topSpacing = when (block.level) {
        1 -> 64.dp
        2 -> 36.dp
        else -> 24.dp
    }

    val bottomSpacing = when (block.level) {
        1 -> 24.dp
        2 -> 16.dp
        else -> 12.dp
    }

    val baseStyle = when (block.level) {
        1 -> MaterialTheme.typography.ChapterTitle
        2 -> MaterialTheme.typography.headlineSmall
        3 -> MaterialTheme.typography.titleLarge
        else -> MaterialTheme.typography.titleMedium
    }

    val titleFontSize = when (block.level) {
        1 -> fontSizeSp + 8
        2 -> fontSizeSp + 5
        3 -> fontSizeSp + 3
        else -> fontSizeSp + 2
    }

    Text(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(
                            top = topSpacing,
                            bottom = bottomSpacing
                    ),
            text = block.text,
            style = baseStyle.copy(
                    fontSize = titleFontSize.sp,
                    lineHeight = (titleFontSize * 1.2f).sp
            ),
            color = TextModalPrimary,
            textAlign = TextAlign.Center
    )
}

@Composable
fun ParagraphBlock(
    block: ReaderContentBlock.Paragraph,
    fontSizeSp: Float,
    modifier: Modifier = Modifier
) {
    Text(
            text = block.text,
            modifier = modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = fontSizeSp.sp,
                    lineHeight = (fontSizeSp * 1.5f).sp,
                    color = TextModalPrimary
            )
    )
}

@Composable
fun QuoteBlock(
    block: ReaderContentBlock.Quote,
    fontSizeSp: Float,
    modifier: Modifier = Modifier
) {
    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
    ) {
        Box(
                modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.outline)
        )

        Text(
                text = block.text,
                style = MaterialTheme.typography.ParagraphItalic.copy(
                        fontSize = fontSizeSp.sp
                ),
                color = TextModalPrimary

                /*.copy(
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * 1.5f).sp,
                        fontStyle = FontStyle.Italic
                ),*/,
                modifier = Modifier
                        .weight(1f)
                        .padding(start = MaterialTheme.spacing.spaceTwelve)
                        .padding(vertical = MaterialTheme.spacing.spaceTwelve)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFDFCF8)
@Composable
private fun TextBlocksPreview() {
    PageKeeperTheme {
        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = MaterialTheme.spacing.spaceTen * 2),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceLargeMedium)
        ) {
            ChapterTitleBlock(
                    block = ReaderContentBlock.ChapterTitle(
                            text = "Chapter One",
                            level = 1
                    ),
                    fontSizeSp = 18f
            )

            ParagraphBlock(
                    block = ReaderContentBlock.Paragraph(
                            text = AnnotatedString(
                                    "It was a bright morning, and the first pages of the journey " +
                                            "were waiting to be read."
                            )
                    ),
                    fontSizeSp = 18f
            )
            QuoteBlock(
                    block = ReaderContentBlock.Quote(
                            text = AnnotatedString(
                                    "A reader lives a thousand lives before he dies."
                            )
                    ),
                    fontSizeSp = 18f
            )
        }
    }
}

