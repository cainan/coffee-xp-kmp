package com.cso.coffeexp.core.util

import java.io.File

val appDataDirectory: File
    get() {
        val userHome = System.getProperty("user.home")
        return when (currentOs) {
            DesktopOs.WINDOWS -> File(System.getenv("APPDATA"), "CoffeeXp")
            DesktopOs.MACOS -> File(userHome, "Library/Application Support/CoffeeXp")
            DesktopOs.LINUX -> File(userHome, ".local/share/CoffeeXp")
        }
    }