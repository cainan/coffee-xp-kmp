package com.cso.coffeexp.core.design_system.components

import androidx.compose.runtime.Composable
import io.github.vinceglb.filekit.PlatformFile

@Composable
actual fun rememberCameraLauncherOrNull(
    onError: (String?) -> Unit,
    onResult: (PlatformFile?) -> Unit
): (() -> Unit)? {
    return null
}