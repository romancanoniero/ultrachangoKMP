package com.iyr.ultrachango

import android.app.Application
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.logger.Level

class UltraChangoApp : Application() {


    companion object {
        lateinit var contextProvider: Context

    }

    override fun onCreate() {
        super.onCreate()
        contextProvider = this.baseContext
        AppContext.context = this
     initKoin {
         //   androidLogger(Level.DEBUG)
            androidContext(this@UltraChangoApp)
        }
    }

}