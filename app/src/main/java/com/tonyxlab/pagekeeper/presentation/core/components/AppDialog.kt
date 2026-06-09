package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.R

@Composable
fun AppDialog(
    onDismissRequest: () -> Unit,
    onConfirm: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    positiveButtonText: String,
    negativeButtonText: String? = null,
    isDeleteDialog: Boolean = false
) {

    AlertDialog(
            modifier = Modifier.padding(
                    vertical = MaterialTheme.spacing.spaceTwelve * 2,
                    horizontal = MaterialTheme.spacing.spaceTwelve * 2
            ),
            containerColor = MaterialTheme.colorScheme.surface,
            onDismissRequest = { onDismissRequest() },
            title = {
                Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dialogTitle,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.TitleMediumMedium,
                        color = MaterialTheme.colorScheme.onSurface
                )
            },
            text = {
                Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = dialogText,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.BodyMediumRegular,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End
                ) {
                    if (!negativeButtonText.isNullOrBlank()) {
                        TextButton(
                                onClick = onDismissRequest,
                        ) {

                            Text(
                                    text = negativeButtonText,
                                    style = MaterialTheme.typography.BodyMediumMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    TextButton(
                            onClick = onConfirm,
                    ) {
                        Text(
                                text = positiveButtonText,
                                style = MaterialTheme.typography.BodyMediumMedium,
                                color = if (isDeleteDialog)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            },
            properties = DialogProperties(usePlatformDefaultWidth = false)
    )
}

@PreviewLightDark
@Composable
private fun AppDialog_Preview() {
    PageKeeperTheme {
        Box(
                modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background),
                contentAlignment = Alignment.Center
        ) {
            AppDialog(
                    dialogTitle = stringResource(id = R.string.dialog_text_delete_book, "Harry Potter"),
                    dialogText = stringResource(id = R.string.dialog_text_remove_action),
                    positiveButtonText = "Delete",
                    negativeButtonText = "Cancel",
                    isDeleteDialog = true,
                    onDismissRequest = {},
                    onConfirm = {},
            )
        }
    }
}
