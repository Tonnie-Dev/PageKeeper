package com.tonyxlab.pagekeeper.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import com.tonyxlab.pagekeeper.utils.AppDefaults
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import okio.IOException

class FontDataStore(private val context: Context)  {

    val fontSize: Flow<Float>
        get() = context.dataStore
                .data
                .catch { e ->

                    if (e is IOException) {

                        emit(emptyPreferences())
                    } else throw e
                }
                .map { prefs ->

                    prefs[FontSizeKey] ?: AppDefaults.DEFAULT_FONT_SIZE
                }

    suspend fun saveFontSize(fontSize: Float) {
        context.dataStore.edit { preferences ->
            preferences[FontSizeKey] = fontSize
        }
    }

    private companion object {
        val FontSizeKey = floatPreferencesKey("font_size")
    }
}
