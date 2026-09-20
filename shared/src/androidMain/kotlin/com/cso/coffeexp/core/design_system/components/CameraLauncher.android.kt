package com.cso.coffeexp.core.design_system.components

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.compose.rememberCameraPickerLauncher

@Composable
actual fun rememberCameraLauncherOrNull(
    onError: (String?) -> Unit,
    onResult: (PlatformFile?) -> Unit
): (() -> Unit)? {
    val launcher = rememberCameraPickerLauncher(
        onError = { error ->
            onError(error.message)
        },
        onResult = { platformFile ->
            onResult(platformFile)
        }
    )
    return { launcher.launch() }
}