package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheetDefaults.properties
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.StateAlert
import com.tonyxlab.pagekeeper.utils.ifThen

@Composable
fun AppDropdownMenu(
    actionOneText: String,
    actionTwoText: String,
    isMenuExpanded: Boolean,
    modifier: Modifier = Modifier,
    isSmallMenu: Boolean = false,
    onDismissMenu: () -> Unit,
    onFirstAction: () -> Unit,
    onSecondAction: () -> Unit
) {
    DropdownMenu(
            modifier = modifier
                    .ifThen(isSmallMenu) {
                        width(SMALL_CONTEXT_MENU_WIDTH)
                    }
                    .ifThen(isSmallMenu.not()) {
                        width(IntrinsicSize.Max)
                    },
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
                text = actionOneText,
                icon = painterResource(R.drawable.ic_edit),
                tintColor = MaterialTheme.colorScheme.onSurface,
                isSmallMenu = isSmallMenu,
                onClick = onFirstAction
        )

        ContextMenuItem(
                text = actionTwoText,
                icon = painterResource(R.drawable.ic_delete),
                tintColor = StateAlert,
                isSmallMenu = isSmallMenu,
                onClick = onSecondAction
        )
    }
}

@Composable
private fun ContextMenuItem(
    text: String,
    icon: Painter,
    tintColor: Color,
    isSmallMenu: Boolean,
    onClick: () -> Unit
) {
    DropdownMenuItem(
            modifier = Modifier
                    .ifThen(!isSmallMenu) {
                        width(IntrinsicSize.Max)
                    },
            text = {
                Text(
                        text = text,
                        color = tintColor,
                        style = MaterialTheme.typography.labelLarge,
                        maxLines = 1,
                        softWrap = false
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
