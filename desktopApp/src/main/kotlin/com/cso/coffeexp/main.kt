package com.cso.coffeexp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cso.coffeexp.di.initKoin

fun main() = application {
    initKoin()

    Window(
        onCloseRequest = ::exitApplication,
        title = "Coffee Xp",
    ) {
        App()
    }
}