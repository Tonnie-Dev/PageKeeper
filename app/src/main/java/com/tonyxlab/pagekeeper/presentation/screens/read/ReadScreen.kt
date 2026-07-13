package com.tonyxlab.pagekeeper.presentation.screens.read

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ChapterTitleBlock
import com.tonyxlab.pagekeeper.presentation.screens.read.components.FontSizeControlPanel
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ParagraphBlock
import com.tonyxlab.pagekeeper.presentation.screens.read.components.QuoteBlock
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReadTopBar
import com.tonyxlab.pagekeeper.presentation.screens.read.components.ReaderBottomBar
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.read.handling.ReadUiState
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.ReaderOrientationEffect
import com.tonyxlab.pagekeeper.utils.SetStatusBarIconsColor
import com.tonyxlab.pagekeeper.utils.rememberIsMobileDevice
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun ReadScreen(
    bookId: String,
    navigator: Navigator,
    viewModel: ReadViewModel = koinViewModel(parameters = { parametersOf(bookId) })
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SetStatusBarIconsColor(darkIcons = true)


    ReaderOrientationEffect(
            orientation = uiState.orientation,
            isMobileDevice = rememberIsMobileDevice()
    )
    BaseContentLayout(
            viewModel = viewModel,
            onBackPressed = { navigator.popToLibrary() },
            topBar = { uiState ->
                AnimatedVisibility(
                        visible = !uiState.immersiveMode,
                        enter = fadeIn(
                                animationSpec = tween(durationMillis = 200)
                        ),
                        exit = fadeOut(
                                animationSpec = tween(durationMillis = 350)
                        )
                ) {
                    ReadTopBar(
                            titleText = uiState.book?.title.orEmpty(),
                            isFavorite = uiState.book?.isFavorite ?: false,
                            modifier = Modifier,
                            onEvent = viewModel::onEvent
                    )
                }
            },
            bottomBar = { uiState ->
                AnimatedVisibility(
                        visible = !uiState.immersiveMode,
                        enter = fadeIn(
                                animationSpec = tween(durationMillis = 200)
                        ),
                        exit = fadeOut(
                                animationSpec = tween(durationMillis = 350)
                        )
                ) {

                    AnimatedContent(
                            uiState.fontSizeState.showFontSizePanel,
                            transitionSpec = {
                                fadeIn(
                                        animationSpec = tween(300)
                                ) togetherWith fadeOut(
                                        animationSpec = tween(200)
                                )
                            },
                            label = "Reading controls"
                    ) { status ->

                        when (status) {
                            true -> {
                                FontSizeControlPanel(
                                        fontSize = uiState.fontSizeState.previewFontSize,
                                        onFontSizeChange = { fontSize ->
                                            viewModel.onEvent(
                                                    ReadUiEvent.PreviewFontSizeChange(
                                                            fontSize
                                                    )
                                            )
                                        },
                                        onFontSizeChangeFinished = { fontSize ->
                                            viewModel.onEvent(
                                                    ReadUiEvent.FontSizeChangeFinished(
                                                            fontSize
                                                    )
                                            )
                                        }
                                )

                            }

                            else -> {
                                ReaderBottomBar(
                                        uiState = uiState,
                                        onEvent = viewModel::onEvent
                                )

                            }
                        }
                    }

                }
            },
            actionEventHandler = { context, actionEvent ->
                when (actionEvent) {
                    is ReadActionEvent.ShowToast -> {
                        Toast.makeText(context, actionEvent.message, Toast.LENGTH_SHORT)
                                .show()
                    }

                    ReadActionEvent.ExitReader -> navigator.popToLibrary()
                }
            }
    ) { uiState ->
        ReadScreenContent(
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun ReadScreenContent(
    uiState: ReadUiState,
    onEvent: (ReadUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val blocks = uiState.document?.blocks.orEmpty()

    val animatedTopPadding by animateDpAsState(
            targetValue = if (uiState.immersiveMode) {
                20.dp
            } else {
                84.dp
            },
            animationSpec = tween(durationMillis = 350),
            label = "readerTopPadding"
    )

    val animatedBottomPadding by animateDpAsState(
            targetValue = if (uiState.immersiveMode) {
                30.dp
            } else {
                100.dp
            },
            animationSpec = tween(durationMillis = 350),
            label = "readerBottomPadding"
    )


    LazyColumn(
            modifier = modifier
                    .animateContentSize(animationSpec = tween(durationMillis = 350))
                    .fillMaxSize()
                    .pointerInput(uiState.immersiveMode) {
                        detectTapGestures {
                            onEvent(ReadUiEvent.ToggleImmersiveMode)
                        }
                    },
            contentPadding = PaddingValues(
                    start = MaterialTheme.spacing.spaceTen * 2,
                    end = MaterialTheme.spacing.spaceTen * 2,
                    top = animatedTopPadding,
                    bottom = animatedBottomPadding
            ),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spaceTen * 2)
    ) {
        items(items = blocks) { block ->
            when (block) {
                is ReaderContentBlock.ChapterTitle -> {
                    ChapterTitleBlock(
                            block = block,
                            fontSizeSp = uiState.fontSizeState.previewFontSize
                    )
                }

                is ReaderContentBlock.Paragraph -> {
                    ParagraphBlock(
                            block = block,
                            fontSizeSp = uiState.fontSizeState.previewFontSize
                    )
                }

                is ReaderContentBlock.Quote -> {
                    QuoteBlock(
                            block = block,
                            fontSizeSp = uiState.fontSizeState.previewFontSize
                    )
                }
            }
        }
    }
}
