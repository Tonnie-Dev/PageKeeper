package com.tonyxlab.pagekeeper.presentation.navigation

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

class Navigator(private val backStack: NavBackStack<NavKey>) {

    private fun push(destination: NavKey) {
        backStack.add(destination)
    }

    fun popToLibrary() {
        while (backStack.size > 1) {
            popBackstack()
        }
    }

    fun navigateToRead(bookId: String) {
        push(ReadDestination(bookId))
    }

    fun popBackstack() {
        if (backStack.size > 1) {
            backStack.removeLastOrNull()
        }
    }
}