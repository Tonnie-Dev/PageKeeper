package com.tonyxlab.pagekeeper.presentation.screens.reader.read

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadingControlMode
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.components.ChapterTitleBlock
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.components.FontSizeControlPanel
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.components.ParagraphBlock
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.components.QuoteBlock
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.components.ReadTopBar
import com.tonyxlab.pagekeeper.presentation.screens.reader.read.components.ReaderBottomBar
import com.tonyxlab.pagekeeper.presentation.theme.spacing
import com.tonyxlab.pagekeeper.utils.ReaderOrientationEffect
import com.tonyxlab.pagekeeper.utils.SetStatusBarIconsColor
import com.tonyxlab.pagekeeper.utils.ifThen
import com.tonyxlab.pagekeeper.utils.rememberIsDeviceWide
import com.tonyxlab.pagekeeper.utils.rememberIsMobileDevice
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun ReadScreen(
    navigator: Navigator,
    viewModel: ReadViewModel,
    navigateToChaptersScreen: () -> Unit
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
                        visible = uiState.controlMode != ReadingControlMode.Immersive,
                        enter = fadeIn(
                                animationSpec = readerControlsTween()
                        ) + expandVertically(
                                animationSpec = readerControlsTween(),
                                expandFrom = Alignment.Top
                        ),
                        exit = fadeOut(
                                animationSpec = readerControlsTween()
                        ) + shrinkVertically(
                                animationSpec = readerControlsTween(),
                                shrinkTowards = Alignment.Top
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
                        visible = uiState.controlMode != ReadingControlMode.Immersive,
                        enter = fadeIn(
                                animationSpec = readerControlsTween()
                        ) + expandVertically(
                                animationSpec = readerControlsTween(),
                                expandFrom = Alignment.Bottom
                        ),
                        exit = fadeOut(
                                animationSpec = readerControlsTween()
                        ) + shrinkVertically(
                                animationSpec = readerControlsTween(),
                                shrinkTowards = Alignment.Bottom
                        )
                ) {

                    AnimatedContent(
                            targetState = uiState.controlMode,
                            transitionSpec = {
                                fadeIn(
                                        animationSpec = tween(300)
                                ) togetherWith fadeOut(
                                        animationSpec = tween(200)
                                )
                            },
                            label = "Reading controls"
                    ) { controlMode ->

                        when (controlMode) {

                            ReadingControlMode.Immersive -> Unit

                            ReadingControlMode.DefaultToolbar -> {
                                ReaderBottomBar(
                                        uiState = uiState,
                                        onEvent = viewModel::onEvent
                                )
                            }

                            ReadingControlMode.FontSizePanel -> {
                                FontSizeControlPanel(
                                        fontSize = uiState.fontSizeState.previewFontSize,
                                        onEvent = viewModel::onEvent
                                )
                            }
                        }
                    }
                }
            },
            actionEventHandler = { context, actionEvent ->
                when (actionEvent) {
                    is ReaderActionEvent.ShowToast -> {
                        Toast.makeText(context, actionEvent.message, Toast.LENGTH_SHORT)
                                .show()
                    }

                    ReaderActionEvent.ExitReader -> navigator.popToLibrary()
                    ReaderActionEvent.NavigateToChaptersView -> navigateToChaptersScreen()
                    ReaderActionEvent.NavigateToBookmarksView -> navigator.navigateToBookmark("")
                    else -> Unit
                }
            }
    ) { uiState ->
        ReadScreenContent(
                modifier = Modifier.ifThen(uiState.controlMode == ReadingControlMode.Immersive) {
                    statusBarsPadding()
                },
                uiState = uiState,
                onEvent = viewModel::onEvent
        )
    }
}

@Composable
fun ReadScreenContent(
    uiState: ReaderUiState,
    onEvent: (ReaderUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {

    val isDeviceWide = rememberIsDeviceWide()
    val maxWidth = if (isDeviceWide) MAX_WIDTH else Dp.Unspecified

    val blocks = uiState.document?.blocks.orEmpty()
    val listState = rememberLazyListState()
    /*
    * save position
    */

    LaunchedEffect(listState) {

        snapshotFlow { listState.firstVisibleItemIndex }
                .distinctUntilChanged()
                .collect { blockIndex ->
                    onEvent(ReaderUiEvent.ReadingPositionChanged(blockIndex))
                }
    }

    /*
    * retrieve position
    */

    LaunchedEffect(uiState.document) {

        val document = uiState.document ?: return@LaunchedEffect
        val savedIndex = uiState.book?.lastReadBlockIndex ?: 0

        if (document.blocks.isNotEmpty()) {

            val safeIndex = savedIndex.coerceIn(0, document.blocks.lastIndex)
            listState.scrollToItem(safeIndex)
        }
    }

    /*
   * jump to position
   */

    LaunchedEffect(uiState.requestedBlockIndex) {

        val targetIndex = uiState.requestedBlockIndex ?: return@LaunchedEffect

        val document = uiState.document ?: return@LaunchedEffect

        if (document.blocks.isEmpty()) return@LaunchedEffect

        val safeIndex =
            targetIndex.coerceIn(0, document.blocks.lastIndex)

        listState.scrollToItem(safeIndex)

        onEvent(ReaderUiEvent.ChaptersJumpConsumed)
    }

    Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
    ) {

        LazyColumn(
                modifier = modifier
                        .widthIn(max = maxWidth)
                        .pointerInput(uiState.controlMode) {
                            detectTapGestures {
                                onEvent(ReaderUiEvent.ReadingAreaClicked)
                            }
                        },
                state = listState,
                contentPadding = PaddingValues(
                        start = MaterialTheme.spacing.spaceTen * 2,
                        end = MaterialTheme.spacing.spaceTen * 2,
                ),
                horizontalAlignment = Alignment.CenterHorizontally,
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
}

private const val READER_ANIMATION_DURATION = 350

private val MAX_WIDTH = 600.dp

private fun <T> readerControlsTween() =
    tween<T>(durationMillis = READER_ANIMATION_DURATION)
