package com.tonyxlab.pagekeeper

import android.app.Application
import com.tonyxlab.pagekeeper.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class PageKeeperApp : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@PageKeeperApp)
            modules(appModule)
        }
    }
}
