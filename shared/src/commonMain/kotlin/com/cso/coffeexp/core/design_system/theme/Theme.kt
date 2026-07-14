package com.cso.coffeexp.core.design_system.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 8dp-based spacing scale (specs/DESIGN.md `spacing` tokens). Identical
// across light/dark themes, so it's a plain static object rather than a
// CompositionLocal - there is no context-dependent value to provide.
object Spacing {
    val base: Dp = 8.dp
    val marginMobile: Dp = 20.dp
    val marginDesktop: Dp = 40.dp
    val gutter: Dp = 16.dp
    val stackSm: Dp = 12.dp
    val stackMd: Dp = 24.dp
    val stackLg: Dp = 48.dp
}

// Design-system theme entry point. Mirrors MaterialTheme's own call-site
// ergonomics: `CoffeeXpTheme { ... }` wraps content, and
// `CoffeeXpTheme.spacing` reads the static spacing scale.
object CoffeeXpTheme {

    val spacing: Spacing = Spacing

    @Composable
    operator fun invoke(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit,
    ) {
        val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

        MaterialTheme(
            colorScheme = colorScheme,
            typography = appTypography(),
            shapes = AppShapes,
            content = content,
        )
    }
}
