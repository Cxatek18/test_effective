package com.team.testeffective.app

import android.app.Application
import com.team.testeffective.di.dataModule
import com.team.testeffective.di.domainModule
import com.team.testeffective.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                listOf(presentationModule, domainModule, dataModule)
            )
        }
    }
}