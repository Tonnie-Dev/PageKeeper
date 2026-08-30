package com.tonyxlab.pagekeeper.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Navigator(private val backStack: NavBackStack<NavKey>) {

    private fun push(destination: NavKey) {
        backStack.add(destination)
    }

    fun navigateToLibrary(destination: AppNavigationDestination = AppNavigationDestination.Library) {
        push(LibraryDestination(destination = destination))
    }
    fun popToLibrary() {
        while (backStack.size > 1) {
            popBackstack()
        }
    }

    fun navigateToRead(bookId: String) {
        push(ReadDestination(bookId))
    }

    fun navigateToBookBookmarks(bookId: String) {
        push(
                ReadDestination(
                        bookId = bookId,
                        startScreen = ReaderStartScreen.Bookmarks
                )
        )
    }

    fun navigateToGlobalBookmarks() {
        push(BookmarksDestination)
    }
    fun popBackstack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}