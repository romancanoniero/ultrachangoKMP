package com.iyr.ultrachango

import AppContext
import android.app.Application
import android.content.Context
import com.google.firebase.FirebaseApp
import com.iyr.ultrachango.utils.shared.firebase.FirebaseConfig
import org.koin.android.ext.koin.androidContext

class UltraChangoApp : Application() {


    companion object {
        lateinit var contextProvider: Context

    }

    override fun onCreate() {
        super.onCreate()
        contextProvider = this.baseContext
        AppContext.context = this

        FirebaseApp.initializeApp(this)
        initKoin {
            //   androidLogger(Level.DEBUG)
            androidContext(this@UltraChangoApp)
        }
    }

}