package com.tonyxlab.pagekeeper.domain.repository

interface ReaderSettingsRepository {

    suspend fun getFontSize(): Float?

    suspend fun saveFontSize(fontSize: Float)
}
