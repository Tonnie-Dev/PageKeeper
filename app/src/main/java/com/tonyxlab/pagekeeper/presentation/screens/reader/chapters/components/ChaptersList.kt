package com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.model.ChapterUiSection
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.TitleSmallMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import timber.log.Timber

@Composable
fun ChaptersList(
    sections: List<ChapterUiSection>,
    expandedIndex: Int,
    currentChapterId: String?,
    onSectionClick: (Int) -> Unit,
    onChapterClick: (ChapterUiItem) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyColumn(modifier = modifier) {

        sections.forEachIndexed { index, section ->


            val isExpanded = index == expandedIndex

            item(key = "section-${section.id}") {

                ChapterSectionHeader(
                        title = section.title,
                        expanded = isExpanded,
                        onClick = { onSectionClick(index) }
                )
            }

            if (isExpanded) {
                items(
                        items = section.chapters,
                        key = { chapter -> "chapter-${chapter.id}" }

                ) { chapter ->

                    Timber.tag("ReadScreen")
                            .i("The tittle is ${chapter.startBlockIndex}")
                    ChapterRow(
                            chapter = chapter,
                            selected = chapter.id == currentChapterId,
                            onClick = { onChapterClick(chapter) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ChapterSectionHeader(
    title: String,
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
            color = MaterialTheme.colorScheme.background
    ) {

        Row(
                modifier = Modifier.padding(
                        horizontal = MaterialTheme.spacing.spaceMedium,
                        vertical = MaterialTheme.spacing.spaceMedium

                ),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    imageVector = if (expanded)
                        Icons.Default.ExpandLess
                    else
                        Icons.Default.ExpandMore,
                    contentDescription = if (expanded)
                        stringResource(id = R.string.cds_text_expand_less)
                    else
                        stringResource(id = R.string.cds_text_expand_more)
            )

            Spacer(modifier = Modifier.width(MaterialTheme.spacing.spaceMedium))

            Text(
                    modifier = Modifier.weight(1f),
                    text = title,
                    style = MaterialTheme.typography.TitleSmallMedium
            )
        }
    }

}

@Composable
private fun ChapterRow(
    chapter: ChapterUiItem,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val containerColor =
        if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            Color.Transparent
        }

    val contentColor =
        if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurface
        }

    Surface(
            modifier = modifier
                    .fillMaxWidth()
                    .clickable(onClick = onClick),
            color = containerColor,
            contentColor = contentColor
    ) {
        Row(
                modifier = Modifier
                        .padding(horizontal = MaterialTheme.spacing.spaceTwelve * 2)
                        .padding(vertical = MaterialTheme.spacing.spaceSmall),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                    text = chapter.title,
                    style = MaterialTheme.typography.BodySmallRegular,
                    fontWeight =
                        if (selected) {
                            FontWeight.SemiBold
                        } else {
                            FontWeight.Normal
                        },
                    modifier = Modifier.weight(1f)
            )

            if (selected) {
                Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "Current chapter",
                        modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}
