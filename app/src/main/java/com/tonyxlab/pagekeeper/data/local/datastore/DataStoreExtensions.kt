package com.tonyxlab.pagekeeper.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

const val DATASTORE_NAME = "page_keeper_datastore"
val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)