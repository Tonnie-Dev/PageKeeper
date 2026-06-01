package com.tonyxlab.pagekeeper

import android.app.Application
import timber.log.Timber

class PageKeeperApp : Application(){
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}