package com.tonyxlab.pagekeeper.presentation.screens.reader.chapters.handling

import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderActionEvent
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderJumpTarget
import com.tonyxlab.pagekeeper.presentation.screens.reader.ReaderUiState

class ChapterHandler(
    private val updateState: ((ReaderUiState) -> ReaderUiState) -> Unit,
    private val sendActionEvent: (ReaderActionEvent) -> Unit,
) {

    fun viewChapters() {
        sendActionEvent(ReaderActionEvent.NavigateToChaptersView)
    }

    fun onConsumeChapterJump() {
        updateState { state -> state.copy(requestedJumpTarget = null) }
    }

    fun exitChapters() {
        sendActionEvent(ReaderActionEvent.ExitChapters)
    }

    fun selectChapter(startBlockIndex: Int) {
        val safeIndex = startBlockIndex.coerceAtLeast(0)

        updateState { state ->
            state.copy(
                    requestedJumpTarget = ReaderJumpTarget.ChapterJumpTarget(
                            blockIndex = safeIndex
                    )
            )
        }
        sendActionEvent(ReaderActionEvent.ExitChapters)
    }
}
