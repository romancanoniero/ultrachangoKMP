package com.iyr.ultrachango

import androidx.compose.ui.window.ComposeUIViewController

fun MainViewController() = ComposeUIViewController(
    configure = { initKoin() }
) {
//   val database: UltraChangoDatabase = getRoomDatabase(getDatabaseBuilder())
    println("Antes")


    App(

     //   database = database
    )
    println("Despues")
}