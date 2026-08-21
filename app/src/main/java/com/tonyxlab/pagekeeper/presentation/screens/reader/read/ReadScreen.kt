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
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tonyxlab.pagekeeper.domain.model.ReaderContentBlock
import com.tonyxlab.pagekeeper.presentation.core.BaseContentLayout
import com.tonyxlab.pagekeeper.presentation.navigation.Navigator
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReadViewModel
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderJumpTarget
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

@Composable
fun ReadScreen(
    navigator: Navigator,
    viewModel: ReadViewModel,
    navigateToChaptersScreen: () -> Unit,
    navigateToBookmarkScreen: () -> Unit
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
                    ReaderActionEvent.NavigateToBookmarksView -> navigateToBookmarkScreen()
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
    val density = LocalDensity.current
    val maxWidth = if (isDeviceWide) MAX_WIDTH else Dp.Unspecified

    val blocks = uiState.readerBook?.blocks.orEmpty()
    val listState = rememberLazyListState()

    val textLayouts = remember {
        mutableStateMapOf<Int, TextLayoutResult>()
    }
    /*
    * save position
    */

    LaunchedEffect(listState) {

        snapshotFlow {
            listState.layoutInfo.visibleItemsInfo.firstOrNull()
        }
                .collect { visibleItem ->

                    visibleItem ?: return@collect

                    val blockIndex = visibleItem.index

                    val layoutResult =
                        textLayouts[blockIndex]

                    val textOffset =
                        if (layoutResult != null) {

                            val localY =
                                (-visibleItem.offset).toFloat()

                            layoutResult.getTextOffsetAtVerticalPosition(
                                    y = localY
                            )

                        } else {
                            0
                        }

                    onEvent(
                            ReaderUiEvent.ReadingPositionChanged(
                                    blockIndex = blockIndex,
                                    textOffset = textOffset
                            )
                    )
                }
    }

    /*
    * retrieve last read position
    */

    LaunchedEffect(uiState.readerBook, uiState.requestedJumpTarget) {

        val readerBook = uiState.readerBook ?: return@LaunchedEffect

        if (uiState.requestedJumpTarget != null) {

            return@LaunchedEffect
        }
        val savedIndex = uiState.book?.lastReadBlockIndex ?: 0

        if (readerBook.blocks.isNotEmpty()) {

            val safeIndex = savedIndex.coerceIn(0, readerBook.blocks.lastIndex)
            listState.scrollToItem(safeIndex)
        }
    }

    /*
   * jump to chapter or bookmark position
   */

    LaunchedEffect(uiState.requestedJumpTarget,textLayouts.size) {

        val target = uiState.requestedJumpTarget
            ?: return@LaunchedEffect

        val readerBook = uiState.readerBook
            ?: return@LaunchedEffect

        if (readerBook.blocks.isEmpty()) return@LaunchedEffect

        val blockIndex = when(target) {
            is ReaderJumpTarget.ChapterJumpTarget -> target.blockIndex
            is ReaderJumpTarget.BookmarkJumpTarget -> target.blockIndex
        }.coerceIn(0, readerBook.blocks.lastIndex)





        when(target) {
            is ReaderJumpTarget.ChapterJumpTarget -> {
                listState.scrollToItem(blockIndex)

                onEvent(ReaderUiEvent.ReaderJumpConsumed)
            }
            is ReaderJumpTarget.BookmarkJumpTarget -> {
                listState.scrollToItem(blockIndex)

                val layoutResult =
                    textLayouts[blockIndex]
                        ?: return@LaunchedEffect

                val safeTextOffset =
                    target.textOffset.coerceIn(
                            0,
                            layoutResult.layoutInput.text.length
                    )

                val line =
                    layoutResult.getLineForOffset(
                            safeTextOffset
                    )

                val bookmarkedLineTop =
                    layoutResult.getLineTop(line)



                val desiredTop =
                    with(density) {
                        BOOKMARK_TOP_OFFSET.toPx()
                    }

                listState.scrollBy(
                        bookmarkedLineTop - desiredTop
                )

                onEvent(
                        ReaderUiEvent.ReaderJumpConsumed
                )
            }
        }
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
            itemsIndexed(items = blocks) { blockIndex, block ->
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
                                fontSizeSp = uiState.fontSizeState.previewFontSize,
                                onTextLayout = { layoutResult ->
                                    textLayouts[blockIndex] = layoutResult
                                }
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

private val BOOKMARK_TOP_OFFSET = 72.dp

private fun <T> readerControlsTween() =
    tween<T>(durationMillis = READER_ANIMATION_DURATION)

private fun TextLayoutResult.getTextOffsetAtVerticalPosition(
    y: Float
): Int {

    if (layoutInput.text.text.isEmpty()) return 0

    val line = getLineForVerticalPosition(
            vertical = y.coerceAtLeast(0f)
    )

    return getLineStart(line)
}