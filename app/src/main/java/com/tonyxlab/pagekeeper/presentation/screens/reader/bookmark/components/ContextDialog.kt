package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.BodyLargeRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun ContextDialog(
    onDismissRequest: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
                modifier = modifier.width(contextDialogWidth),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = contextDialogElevation,
        ) {
            Column(
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.spaceDoubleDp),
            ) {
                ContextDialogItem(
                        text = stringResource(R.string.menu_text_edit),
                        iconResource = R.drawable.ic_edit,
                        contentColor = MaterialTheme.colorScheme.onSurface,
                        onClick = onEdit,
                )

                ContextDialogItem(
                        text = stringResource(R.string.menu_text_delete),
                        iconResource = R.drawable.ic_delete,
                        contentColor = MaterialTheme.colorScheme.error,
                        onClick = onDelete,
                )
            }
        }
    }
}

@Composable
private fun ContextDialogItem(
    text: String,
    iconResource: Int,
    contentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Row(
            modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = contextDialogItemHeight)
                    .clickable(role = Role.Button, onClick = onClick)
                    .padding(horizontal = MaterialTheme.spacing.spaceTen * 2),
            verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
                modifier = Modifier.size(contextDialogIconSize),
                painter = painterResource(iconResource),
                contentDescription = null,
                tint = contentColor,
        )

        Spacer(Modifier.width(MaterialTheme.spacing.spaceMedium))

        Text(
                text = text,
                style = MaterialTheme.typography.BodyLargeRegular,
                color = contentColor,
        )
    }
}

private val contextDialogWidth = 128.dp
private val contextDialogItemHeight = 48.dp
private val contextDialogIconSize = 24.dp
private val contextDialogElevation = 8.dp

@PreviewLightDark
@Composable
private fun ContextDialogPreview() {
    PageKeeperTheme {
        ContextDialog(
                onDismissRequest = {},
                onEdit = {},
                onDelete = {},
        )
    }
}

