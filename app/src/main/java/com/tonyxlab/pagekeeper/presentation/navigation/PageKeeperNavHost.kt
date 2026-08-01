package com.tonyxlab.pagekeeper.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.tonyxlab.pagekeeper.presentation.screens.library.LibraryScreen
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderFlow

@Composable
fun PageKeeperNavHost() {

    val backStack = rememberNavBackStack(LibraryDestination)
    val navigator = remember(backStack) { Navigator(backStack) }

    val entryProvider = entryProvider {

        entry<LibraryDestination> {
            LibraryScreen(navigator = navigator)
        }

        entry<ReadDestination> { key ->
            ReaderFlow(bookId = key.bookId, navigator = navigator)
        }
    }

    NavDisplay(
            backStack = backStack,
            entryProvider = entryProvider,
            entryDecorators = listOf(
                    rememberSaveableStateHolderNavEntryDecorator(),
                    rememberViewModelStoreNavEntryDecorator()
            )
    )
}





