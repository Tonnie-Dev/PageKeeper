package com.tonyxlab.pagekeeper.presentation.screens.reader

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.navigation.ReaderStartScreen
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.BookmarkScreen
import com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.ChaptersScreen
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.ReadScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReaderFlow(
    bookId: String,
    navigator: Navigator,
    startScreen: ReaderStartScreen = ReaderStartScreen.Read
) {
    val viewModel: ReadViewModel = koinViewModel(
            key = "reader-$bookId",
            parameters = { parametersOf(bookId) }
    )

    var currentScreen by rememberSaveable {

        mutableStateOf(
                when (startScreen) {
                    ReaderStartScreen.Read -> ReaderScreen.Read
                    ReaderStartScreen.Bookmarks -> ReaderScreen.Bookmarks
                }
        )

    }

    when (currentScreen) {
        ReaderScreen.Read -> {

            ReadScreen(
                    navigator = navigator,
                    viewModel = viewModel,
                    navigateToChaptersScreen = { currentScreen = ReaderScreen.Chapters },
                    navigateToBookmarkScreen = { currentScreen = ReaderScreen.Bookmarks }
            )
        }

        ReaderScreen.Chapters -> {
            ChaptersScreen(
                    viewModel = viewModel,
                    navigateToReadScreen = { currentScreen = ReaderScreen.Read }
            )
        }

        ReaderScreen.Bookmarks -> {
            BookmarkScreen(
                    viewModel = viewModel,
                    navigateToReadScreen = {

                        when(startScreen) {
                            ReaderStartScreen.Read -> currentScreen = ReaderScreen.Read
                            ReaderStartScreen.Bookmarks -> {navigator.popBackstack()}
                        }
                    }
            )
        }
    }
}

private enum class ReaderScreen {
    Read, Chapters, Bookmarks
}