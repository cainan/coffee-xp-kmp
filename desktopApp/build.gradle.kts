import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

dependencies {
    implementation(projects.shared)

    implementation(libs.filekit.core)

    implementation(compose.desktop.currentOs)
    implementation(libs.kotlinx.coroutinesSwing)

    implementation(libs.compose.uiToolingPreview)
}

compose.desktop {
    application {
        mainClass = "com.cso.coffeexp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cso.coffeexp"
            packageVersion = "1.0.0"
            macOS { iconFile.set(project.file("src/main/resources/coffee-xp-icon.icns")) }
            windows { iconFile.set(project.file("src/main/resources/coffee-xp-icon.ico")) }
            linux { iconFile.set(project.file("src/main/resources/coffee-xp-icon.png")) }
        }
    }
}
