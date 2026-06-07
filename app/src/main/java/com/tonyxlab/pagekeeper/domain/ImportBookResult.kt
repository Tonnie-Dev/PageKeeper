package com.tonyxlab.pagekeeper.domain

sealed interface ImportBookResult {
    data object Success : ImportBookResult
    data object Duplicate : ImportBookResult
    data object UnsupportedFormat : ImportBookResult
    data object Loading : ImportBookResult
    data class Error(val message: String) : ImportBookResult
}