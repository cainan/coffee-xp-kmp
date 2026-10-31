package com.cso.coffeexp

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.runtime.LaunchedEffect
import com.cso.coffeexp.di.initKoin
import io.github.vinceglb.filekit.FileKit
import javax.imageio.ImageIO

fun main() {
    FileKit.init(appId = "CoffeeXp")
    initKoin()
    val windowIcons = listOf(16, 20, 24, 32, 40, 48, 64, 128, 256).map { size ->
        val resource = checkNotNull(Thread.currentThread().contextClassLoader
            .getResourceAsStream("icons/coffee-xp-$size.png"))
        resource.use { ImageIO.read(it) }
    }

    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Coffee Xp",
        ) {
            LaunchedEffect(window) {
                window.iconImages = windowIcons
            }
            App()
        }
    }
}
