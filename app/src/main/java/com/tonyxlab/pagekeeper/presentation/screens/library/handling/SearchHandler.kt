@file:OptIn(FlowPreview::class)

package com.tonyxlab.pagekeeper.presentation.screens.library.handling

import androidx.compose.runtime.snapshotFlow
import com.tonyxlab.pagekeeper.domain.repository.BookRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class SearchHandler(
    private val bookRepository: BookRepository,
    private val coroutineScope: CoroutineScope,
    private val currentState: () -> LibraryUiState,
    private val updateState: ((LibraryUiState) -> LibraryUiState) -> Unit,
) {


    fun observeSearchQuery() {
        snapshotFlow {
            currentState().searchState.searchTextFieldState.text
        }
                .debounce(SEARCH_DEBOUNCE)
                .map { it.toString() }
                .distinctUntilChanged()
                .onEach { query ->
                    onSearchQueryChange(query = query)
                }
                .launchIn(coroutineScope)
    }

    fun enterSearchMode() {
        updateState {
            it.copy(
                    searchState = it.searchState.copy(
                            isSearchMode = true
                    )
            )
        }
    }

    fun exitSearchMode() {
        clearSearchText()
        updateState {
            it.copy(
                    searchState = it.searchState.copy(
                            isSearchMode = false,
                            searchResults = emptyList()
                    )
            )
        }
    }

    fun clearSearchText() {
        currentState().searchState.searchTextFieldState.edit {
            replace(0, length, "")
        }
    }

    private fun onSearchQueryChange(query: String) {
        val cleanQuery = query.trim()

        if (cleanQuery.isBlank()) {
            clearSearchResults()
            return
        }

        coroutineScope.launch {
            val searchResults = bookRepository.searchBooks(cleanQuery)
                    .first()

            updateState {
                it.copy(
                        searchState = it.searchState.copy(
                                searchResults = searchResults
                        )
                )
            }
        }
    }

    private fun clearSearchResults() {
        updateState {
            it.copy(
                    searchState = it.searchState.copy(
                            searchResults = emptyList()
                    )
            )
        }
    }

    private companion object {
        val SEARCH_DEBOUNCE = 300.milliseconds
    }
}