package com.fiap.grainall



import android.app.Application

import com.fiap.grainall.di.appModules

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class GrainallApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@GrainallApplication)
            modules(appModules)
        }
    }

}