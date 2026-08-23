package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.StateAlert

@Composable
fun<T> AppDropdownMenu(
    item:T,
    isMenuExpanded: Boolean,
    modifier: Modifier = Modifier,
    isSmallMenu: Boolean = false,
    onDismissMenu: () -> Unit,
    onFirstAction: () -> Unit,
    onSecondAction: () -> Unit
) {
    DropdownMenu(
            modifier = modifier.width(if (isSmallMenu) SMALL_CONTEXT_MENU_WIDTH else Dp.Unspecified),
            expanded = isMenuExpanded,
            onDismissRequest = onDismissMenu,
            shape = MaterialTheme.shapes.large,
            containerColor = MaterialTheme.colorScheme.surface,
            shadowElevation = SHADOW_ELEVATION,
            properties = PopupProperties(
                    focusable = false,
                    dismissOnClickOutside = true
            )
    ) {
        ContextMenuItem(
                text = stringResource(id = R.string.menu_text_edit),
                icon = painterResource(R.drawable.ic_edit),
                tintColor = MaterialTheme.colorScheme.onSurface,
                onClick = onFirstAction
        )

        ContextMenuItem(
                text = stringResource(id = R.string.menu_text_delete),
                icon = painterResource(R.drawable.ic_delete),
                tintColor = StateAlert,
                onClick = onSecondAction
        )
    }
}

@Composable
private fun ContextMenuItem(
    text: String,
    icon: Painter,
    tintColor: Color,
    onClick: () -> Unit
) {
    DropdownMenuItem(
            text = {
                Text(
                        text = text,
                        color = tintColor,
                        style = MaterialTheme.typography.labelLarge
                )
            },
            leadingIcon = {
                Icon(
                        painter = icon,
                        contentDescription = null,
                        tint = tintColor
                )
            },
            onClick = onClick
    )
}

private val SHADOW_ELEVATION = 6.dp
private val SMALL_CONTEXT_MENU_WIDTH = 124.dp