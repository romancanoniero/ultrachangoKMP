package com.iyr.ultrachango

import androidx.compose.ui.window.ComposeUIViewController
import com.iyr.ultrachango.firebase.FirebaseConfig

fun MainViewController() = ComposeUIViewController(
    configure = {
        FirebaseConfig.initialize()

        initKoin() 
    }
) {
//   val database: UltraChangoDatabase = getRoomDatabase(getDatabaseBuilder())
    App()
}

