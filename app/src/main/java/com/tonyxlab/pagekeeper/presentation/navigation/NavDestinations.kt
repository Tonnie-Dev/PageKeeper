package com.tonyxlab.pagekeeper.presentation.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
data object LibraryDestination : NavKey

@Serializable
data class ReadDestination(
    val bookId: String,
        val startScreen: ReaderStartScreen = ReaderStartScreen.Read
) : NavKey

@Serializable
data object BookmarksDestination : NavKey

@Serializable
enum class ReaderStartScreen{

    Read,Bookmarks
}
