package com.cso.coffeexp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.cso.coffeexp.di.initKoin
import io.github.vinceglb.filekit.FileKit

fun main() {
    FileKit.init(appId = "CoffeeXp")
    initKoin()

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Coffee Xp",
        ) {
            App()
        }
    }
}