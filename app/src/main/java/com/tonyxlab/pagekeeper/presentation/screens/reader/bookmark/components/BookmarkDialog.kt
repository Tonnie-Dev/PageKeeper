package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.domain.model.BookmarkColor
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.toColor
import com.tonyxlab.pagekeeper.presentation.theme.BodyLargeRegular
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium

/**
 * Dialog used to create or edit a bookmark.
 *
 * The title and selected color are owned by the caller so the dialog can be
 * driven directly from screen state.
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BookmarkDialog(
    title: String ,
    selectedColor: BookmarkColor,
    onTitleChange: (String) -> Unit,
    onColorSelected: (BookmarkColor) -> Unit,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isColorMenuExpanded by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .widthIn(max = 356.dp),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Column(modifier = Modifier.padding(28.dp)) {
                Text(
                    text = stringResource(R.string.dialog_text_add_bookmark),
                    style = MaterialTheme.typography.TitleMediumMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(18.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = onTitleChange,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.label_text_title)) },
                    textStyle = MaterialTheme.typography.BodyLargeRegular,
                    minLines = 2,
                    maxLines = 2,
                    shape = MaterialTheme.shapes.large,
                )

                Spacer(Modifier.height(16.dp))

                ExposedDropdownMenuBox(
                    expanded = isColorMenuExpanded,
                    onExpandedChange = { isColorMenuExpanded = it },
                ) {
                    OutlinedTextField(
                        value = selectedColor.displayName(),
                        onValueChange = {},
                        modifier = Modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                        readOnly = true,
                        singleLine = true,
                        textStyle = MaterialTheme.typography.BodyLargeRegular,
                        leadingIcon = {
                            ColorIndicator(color = selectedColor.toColor())
                        },
                        trailingIcon = {
                            if (isColorMenuExpanded) {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = true)
                            } else {
                                androidx.compose.material3.Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                    contentDescription = stringResource(R.string.cds_text_expand_more),
                                )
                            }
                        },
                        shape = MaterialTheme.shapes.large,
                    )

                    ExposedDropdownMenu(
                        expanded = isColorMenuExpanded,
                        onDismissRequest = { isColorMenuExpanded = false },
                    ) {
                        BookmarkColor.entries.forEach { color ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = color.displayName(),
                                        style = MaterialTheme.typography.BodyLargeRegular,
                                    )
                                },
                                onClick = {
                                    onColorSelected(color)
                                    isColorMenuExpanded = false
                                },
                                leadingIcon = { ColorIndicator(color = color.toColor()) },
                                modifier = if (color == selectedColor) {
                                    Modifier.background(MaterialTheme.colorScheme.surfaceVariant)
                                } else {
                                    Modifier
                                },
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(22.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(
                            text = stringResource(R.string.text_button_cancel),
                            style = MaterialTheme.typography.BodyMediumMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    TextButton(onClick = onSave, enabled = title.isNotBlank()) {
                        Text(
                            text = stringResource(R.string.text_button_save),
                            style = MaterialTheme.typography.BodyMediumMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ColorIndicator(color: Color) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(color),
    )
}

@Composable
private fun BookmarkColor.displayName(): String = when (this) {
    BookmarkColor.Blue -> stringResource(R.string.bookmark_color_blue)
    BookmarkColor.Green -> stringResource(R.string.bookmark_color_green)
    BookmarkColor.Yellow -> stringResource(R.string.bookmark_color_yellow)
    BookmarkColor.Orange -> stringResource(R.string.bookmark_color_red)
    BookmarkColor.Purple -> stringResource(R.string.bookmark_color_purple)
}

@Preview(showBackground = true)
@Composable
private fun BookmarkDialogPreview() {
    PageKeeperTheme {
        BookmarkDialog(
            title = "The forest was unusually quiet that evening",
            selectedColor = BookmarkColor.Blue,
            onTitleChange = {},
            onColorSelected = {},
            onDismissRequest = {},
            onSave = {},
        )
    }
}

