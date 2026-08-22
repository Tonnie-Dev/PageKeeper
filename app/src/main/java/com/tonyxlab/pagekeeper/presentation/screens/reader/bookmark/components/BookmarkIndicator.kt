package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.BookmarkUiItem
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.toColor
import com.tonyxlab.pagekeeper.R
@Composable
 fun BookmarkIndicator(
    bookmark: BookmarkUiItem
) {
    Icon(
            painter = painterResource(
                    R.drawable.ic_bookmark_filled
            ),
            contentDescription = stringResource(
                    R.string.cds_bookmarked_position
            ),
            tint = bookmark.color.toColor()
    )
}