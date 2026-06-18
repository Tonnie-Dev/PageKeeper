package com.tonyxlab.pagekeeper.presentation.core.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.tonyxlab.pagekeeper.R
import com.tonyxlab.pagekeeper.presentation.theme.BodyLargeRegular
import com.tonyxlab.pagekeeper.presentation.theme.PageKeeperTheme
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.clickableWithoutRipple

@Composable
fun AppInputField(
    textFieldState: TextFieldState,
    placeholderText: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.BodyLargeRegular,
    placeholderTextStyle: TextStyle = MaterialTheme.typography.BodyLargeRegular,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = RoundedCornerShape(MaterialTheme.spacing.spaceExtraSmall * 7),
    leadingIcon: (@Composable () -> Unit)? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
) {

    var focused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    Box(
            modifier = modifier
                    .fillMaxWidth()
                    .height(MaterialTheme.spacing.spaceTwelve * 6)
                    .background(
                            color = backgroundColor,

                            )
                    .focusRequester(focusRequester)
                    .onFocusChanged { focused = it.isFocused }

                    .padding(horizontal = MaterialTheme.spacing.spaceMedium)
                    .padding(vertical = MaterialTheme.spacing.spaceExtraSmall)
                    .clickableWithoutRipple {
                        focusRequester.requestFocus()
                    },
            contentAlignment = Alignment.Center
    ) {
        Row(

                verticalAlignment = Alignment.CenterVertically,

                ) {
            leadingIcon?.invoke()

            BasicTextField(
                    modifier = Modifier
                            .weight(1f),
                    state = textFieldState,
                    textStyle = textStyle.copy(color = MaterialTheme.colorScheme.onSurface),
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

            trailingIcon?.invoke()
        }
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
    Box(modifier = modifier
            .fillMaxWidth()
            .padding(start = MaterialTheme.spacing.spaceSmall)) {
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
                    textFieldState = TextFieldState(initialText = "Hello"),
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

                    })
        }
    }
}

