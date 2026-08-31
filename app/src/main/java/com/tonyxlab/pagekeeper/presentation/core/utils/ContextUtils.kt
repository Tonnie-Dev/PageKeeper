package com.tonyxlab.pagekeeper.presentation.core.utils

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.provider.OpenableColumns
import android.widget.Toast
import com.tonyxlab.pagekeeper.presentation.screens.library.handling.LibraryUiState

 fun Context.getDisplayName(uri: Uri): String {
    val cursor: Cursor? = contentResolver.query(
            uri,
            null,
            null,
            null,
            null
    )
    cursor?.use {
        val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        if (nameIndex >= 0 && it.moveToFirst()) {
            return it.getString(nameIndex)
                    .orEmpty()
        }
    }
    return uri.lastPathSegment.orEmpty()
}

 fun Context.shareBook(bookId: String, uiState: LibraryUiState) {
    val book = uiState.books.firstOrNull { it.id == bookId }
    if (book == null) {
        Toast.makeText(this, "Unable to share book.", Toast.LENGTH_SHORT)
                .show()
        return
    }

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, book.title)
        putExtra(Intent.EXTRA_TEXT, "${book.title} by ${book.author}")
    }
    startActivity(Intent.createChooser(intent, null))
}
fun Context.getString(stringResId: Int): String {
    return resources.getString(stringResId)
}
