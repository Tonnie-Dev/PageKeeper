package com.tonyxlab.pagekeeper.presentation.core.utils

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberFilePicker(
    onFileSelected: (uri: Uri, fileName: String) -> Unit
): ManagedActivityResultLauncher<String, Uri?> {

    val context = LocalContext.current
    return rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
    ) { uri ->

        uri ?: return@rememberLauncherForActivityResult

        onFileSelected(
                uri,
                context.getDisplayName(uri)
        )

    }
}