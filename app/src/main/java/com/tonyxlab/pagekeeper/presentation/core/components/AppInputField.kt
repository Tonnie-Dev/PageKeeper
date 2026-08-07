package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.BodyLargeRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import kotlinx.coroutines.android.awaitFrame

@Composable
fun AppInputField(
    textFieldState: TextFieldState,
    placeholderText: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.BodyLargeRegular,
    placeholderTextStyle: TextStyle = MaterialTheme.typography.BodyLargeRegular,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    height: Dp = MaterialTheme.spacing.spaceTwelve * 6,
    requestFocusOnStart: Boolean = true,
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    labelText: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {

    var focused by remember { mutableStateOf(true) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(requestFocusOnStart) {
        if (requestFocusOnStart) {
            awaitFrame()
            focusRequester.requestFocus()
        }
    }

    Row(
            modifier = modifier
                    .fillMaxWidth()
                    .height(height)
                    .background(color = backgroundColor)
                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall),
            verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon?.invoke()

        Column {
            labelText?.invoke()

            Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceExtraSmall))

            BasicTextField(
                    modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester)
                            .onFocusChanged { focused = it.isFocused },
                    state = textFieldState,
                    textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
                    lineLimits = TextFieldLineLimits.MultiLine(
                            minHeightInLines = 2,
                            maxHeightInLines = 3
                    ),
                    cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                    keyboardOptions = keyboardOptions,
                    decorator = { innerTextField ->
                        TextDecorator(
                                isEmpty = textFieldState.text.isEmpty(),
                                innerTextField = innerTextField,
                                focused = focused,
                                placeholderText = placeholderText,
                                placeholderTextStyle = placeholderTextStyle.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                        )
                    }
            )
        }
        trailingIcon?.invoke()
    }
}

@Composable
fun TextDecorator(
    isEmpty: Boolean,
    focused: Boolean,
    placeholderText: String,
    placeholderTextStyle: TextStyle,
    innerTextField: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
            modifier = modifier
                    .fillMaxWidth()
                    .padding(start = MaterialTheme.spacing.spaceSmall)
    ) {
        if (isEmpty && !focused) {
            Text(
                    text = placeholderText,
                    style = placeholderTextStyle
            )
        } else {
            innerTextField()
        }
    }
}

@PreviewLightDark
@Composable
private fun SearchComponent_Preview() {

    PageKeeperTheme {

        Column(
                modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .fillMaxSize()
                        .padding(MaterialTheme.spacing.spaceMedium),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceMedium)
        ) {

            AppInputField(
                    modifier = Modifier
                            .clip(MaterialTheme.shapes.extraLarge)
                            .background(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = MaterialTheme.shapes.extraLarge
                            ),
                    textFieldState = TextFieldState(initialText = "Hello"),
                    height = 40.dp,
                    placeholderText = "",
                    leadingIcon = {
                        Column {

                            Image(
                                    painter = painterResource(R.drawable.ic_back),
                                    contentDescription = stringResource(id = R.string.cds_text_back),
                            )
                        }
                    },
                    trailingIcon = {
                        Image(
                                painter = painterResource(R.drawable.ic_cancel),
                                contentDescription = stringResource(id = R.string.cds_text_back),
                        )
                    }
            )
        }
    }
}

