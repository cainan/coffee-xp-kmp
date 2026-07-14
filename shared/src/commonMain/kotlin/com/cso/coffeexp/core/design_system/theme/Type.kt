package com.cso.coffeexp.core.design_system.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.libre_caslon_text_bold
import coffeexp.shared.generated.resources.libre_caslon_text_semibold
import coffeexp.shared.generated.resources.plus_jakarta_sans_regular
import coffeexp.shared.generated.resources.plus_jakarta_sans_semibold
import org.jetbrains.compose.resources.Font

// Editorial/display serif. Only semibold/bold are used: every display and
// headline style in specs/DESIGN.md uses one of those two weights.
@Composable
private fun libreCaslonTextFamily(): FontFamily = FontFamily(
    Font(Res.font.libre_caslon_text_semibold, FontWeight.SemiBold),
    Font(Res.font.libre_caslon_text_bold, FontWeight.Bold),
)

// Body/UI sans. Only regular/semibold are used: every body, title and label
// style in specs/DESIGN.md uses one of those two weights.
@Composable
private fun plusJakartaSansFamily(): FontFamily = FontFamily(
    Font(Res.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(Res.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
)

// Only 6 of these 15 slots are defined by specs/DESIGN.md (displayLarge,
// displayMedium, headlineMedium, bodyLarge, bodyMedium, labelSmall) - mapped
// exactly. The rest are derived within the same family/weight register so
// every slot still resolves to the right font: display/headline -> serif,
// title/body/label -> sans.
@Composable
fun appTypography(): Typography {
    val serif = libreCaslonTextFamily()
    val sans = plusJakartaSansFamily()

    return Typography(
        displayLarge = TextStyle(
            fontFamily = serif,
            fontWeight = FontWeight.Bold,
            fontSize = 40.sp,
            lineHeight = 48.sp,
            letterSpacing = (-0.02).em,
        ),
        displayMedium = TextStyle(
            fontFamily = serif,
            fontWeight = FontWeight.Bold,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            letterSpacing = 0.sp,
        ),
        displaySmall = TextStyle(
            fontFamily = serif,
            fontWeight = FontWeight.Bold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            letterSpacing = 0.sp,
        ),
        headlineLarge = TextStyle(
            fontFamily = serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 26.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        ),
        headlineMedium = TextStyle(
            fontFamily = serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp,
        ),
        headlineSmall = TextStyle(
            fontFamily = serif,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        ),
        titleLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 20.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        ),
        titleMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        ),
        titleSmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        ),
        bodyLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 18.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
        ),
        bodyMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        ),
        labelLarge = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.sp,
        ),
        labelMedium = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            letterSpacing = 0.sp,
        ),
        labelSmall = TextStyle(
            fontFamily = sans,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.05.em,
        ),
    )
}
