package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.BodyMediumMedium
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing

@Composable
fun AppButton(
    buttonText: String,
    leadingIcon: Painter,
    modifier: Modifier = Modifier,
    buttonColor: Color = MaterialTheme.colorScheme.primary,
    buttonTextStyle: TextStyle = MaterialTheme.typography.BodyMediumMedium,
    buttonTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    onClick: () -> Unit,
) {
    Box(
            modifier = modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(buttonColor)
                    .clickable(onClick = onClick)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceTen),
            contentAlignment = Alignment.Center
    ) {
        Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                    modifier = Modifier,
                    painter = leadingIcon,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    contentDescription = stringResource(id = R.string.button_text_import_book)
            )

            Text(
                    text = buttonText,
                    style = buttonTextStyle,
                    color = buttonTextColor,
                    maxLines = 1
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppButton_Preview() {
    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(MaterialTheme.spacing.spaceMedium)
        ) {

            AppButton(
                    buttonText = "Continue",
                    leadingIcon = painterResource(id = R.drawable.ic_import_book)
            ) { }

        }
    }
}

