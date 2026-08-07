package com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import com.tonyxlab.pagekeeper.presentation.core.components.AppInputField
import com.tonyxlab.pagekeeper.presentation.screens.reader.bookmark.model.toColor
import com.tonyxlab.pagekeeper.presentation.theme.BgModalInput
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumRegular
import com.tonyxlab.pagekeeper.presentation.theme.BodySmallRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.TitleMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BookmarkDialog(
    textFieldState: TextFieldState,
    selectedColor: BookmarkColor,
    onColorSelected: (BookmarkColor) -> Unit,
    onDismissRequest: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
) {

    var isColorMenuExpanded by remember { mutableStateOf(false) }

    val hasText = textFieldState.text.isNotEmpty()

    val fieldShape = MaterialTheme.shapes.large
    val borderColor = MaterialTheme.colorScheme.outlineVariant
    val borderWidth = MaterialTheme.spacing.spaceSingleDp

    Dialog(
            onDismissRequest = {
                isColorMenuExpanded = false
                onDismissRequest()
            },
            properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
                modifier = modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.spaceTwelve * 2)
                        .widthIn(max = dialogMaxWidth),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.surface,
        ) {
            Column(modifier = Modifier.padding(MaterialTheme.spacing.spaceDoubleDp * 14)) {

                Text(
                        text = stringResource(R.string.dialog_text_add_bookmark),
                        style = MaterialTheme.typography.TitleMediumMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                )

                Spacer(Modifier.height(MaterialTheme.spacing.spaceTen * 2))

                AppInputField(
                        modifier = Modifier
                                .border(
                                        width = borderWidth,
                                        color = borderColor,
                                        shape = fieldShape
                                ),
                        textFieldState = textFieldState,
                        placeholderText = stringResource(id = R.string.label_text_title),
                        height = inputFieldHeight,
                        textStyle = MaterialTheme.typography.BodyMediumRegular.copy(
                                color = MaterialTheme.colorScheme.onSurface
                        ),
                        backgroundColor = BgModalInput,
                        labelText = {
                            Text(
                                    modifier = Modifier
                                            .padding(start = MaterialTheme.spacing.spaceSmall)
                                            .padding(top = MaterialTheme.spacing.spaceSmall),
                                    text = stringResource(id = R.string.label_text_title),
                                    style = MaterialTheme.typography.BodySmallRegular,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                )

                Spacer(Modifier.height(MaterialTheme.spacing.spaceMedium))

                ExposedDropdownMenuBox(
                        expanded = isColorMenuExpanded,
                        onExpandedChange = { isColorMenuExpanded = it },
                ) {

                    OutlinedTextField(
                            modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                            width = borderWidth,
                                            color = borderColor,
                                            shape = fieldShape,
                                    )
                                    .menuAnchor(
                                            type = ExposedDropdownMenuAnchorType.PrimaryNotEditable,
                                    ),
                            value = selectedColor.displayName(),
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            textStyle = MaterialTheme.typography.BodyMediumRegular.copy(
                                    color = MaterialTheme.colorScheme.onSurface
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    disabledBorderColor = Color.Transparent,
                                    errorBorderColor = Color.Transparent,
                                    focusedContainerColor = BgModalInput,
                                    unfocusedContainerColor = BgModalInput,
                            ),
                            leadingIcon = {
                                ColorIndicator(color = selectedColor.toColor())
                            },
                            trailingIcon = {
                                if (isColorMenuExpanded) {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = true)
                                } else {
                                    Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                                            contentDescription = stringResource(R.string.cds_text_expand_more),
                                    )
                                }
                            },
                            shape = MaterialTheme.shapes.large,
                    )

                    ExposedDropdownMenu(
                            modifier = Modifier,
                            expanded = isColorMenuExpanded,
                            shape = MaterialTheme.shapes.large,
                            containerColor = MaterialTheme.colorScheme.surface,
                            tonalElevation = 0.dp,
                            shadowElevation = 8.dp,
                            onDismissRequest = { isColorMenuExpanded = false },
                    ) {
                        BookmarkColor.entries.forEach { color ->
                            DropdownMenuItem(
                                    modifier = if (color == selectedColor) {
                                        Modifier
                                                .padding(MaterialTheme.spacing.spaceSmall)
                                                .background(
                                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                                        shape = MaterialTheme.shapes.small
                                                )

                                    } else {
                                        Modifier.background(MaterialTheme.colorScheme.surface)
                                    },
                                    text = {
                                        Text(
                                                text = color.displayName(),
                                                style = MaterialTheme.typography.BodyMediumRegular.copy(
                                                        color = MaterialTheme.colorScheme.onSurface
                                                )
                                        )
                                    },
                                    onClick = {
                                        onColorSelected(color)
                                        isColorMenuExpanded = false
                                    },
                                    leadingIcon = { ColorIndicator(color = color.toColor()) }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(MaterialTheme.spacing.spaceTen * 2))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                Spacer(Modifier.height(MaterialTheme.spacing.spaceTen * 2))

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
                    TextButton(
                            onClick = onSave,
                            enabled = textFieldState.text.isNotEmpty()
                    ) {
                        Text(
                                text = stringResource(R.string.text_button_save),
                                style = MaterialTheme.typography.BodyMediumMedium,
                                color = if (hasText) MaterialTheme.colorScheme.onSurface else
                                    Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}

private val dialogMaxWidth = 312.dp
private val inputFieldHeight = 90.dp

@Composable
private fun ColorIndicator(color: Color) {
    Box(
            modifier = Modifier
                    .size(MaterialTheme.spacing.spaceMedium)
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
                textFieldState = TextFieldState(initialText = "Hello There"),
                selectedColor = BookmarkColor.Blue,
                onColorSelected = {},
                onDismissRequest = {},
                onSave = {},
        )
    }
}

