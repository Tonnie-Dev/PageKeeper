package com.tonyxlab.pagekeeper.presentation.screens.bookmarks

import androidx.compose.runtime.Composable
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BookmarksScreen(
    viewModel: BookmarksViewModel = koinViewModel(),
) {
    BaseContentLayout(viewModel = viewModel) { }
}
