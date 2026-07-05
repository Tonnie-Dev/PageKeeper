package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun <T> LazyListComponent(
    items: List<T>,
    key: (T) -> Any,
    modifier: Modifier = Modifier,
    isDeviceWide: Boolean = false,
    content: @Composable (T) -> Unit,
) {
    val listState = rememberSaveable(saver = LazyListState.Saver) {
        LazyListState()
    }
    val gridState = rememberSaveable(saver = LazyGridState.Saver) {
        LazyGridState()
    }
    when {
        isDeviceWide -> {
            LazyVerticalGrid(
                    modifier = modifier
                            .fillMaxSize(),
                    columns = GridCells.Fixed(count = 2),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                    state = gridState
            ) {

                items(items = items, key = { item -> key(item) }) { item ->
                    content(item)
                }
            }
        }

        else -> {

            LazyColumn(
                    modifier = modifier
                            .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceSmall),
                    state = listState
            ) {

                items(items = items, key = { item -> key(item) }) { item ->
                    content(item)
                }
            }
        }
    }
}
