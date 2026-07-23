package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import kotlin.math.abs
import kotlin.math.round
import kotlin.math.roundToInt

/**
 * Renders a row of stars. Pass [onRatingChange] to make it an interactive
 * 1..[maxStars] selector (New Entry); omit it for a read-only display (Details).
 */
@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    maxStars: Int = 5,
    onRatingChange: ((Int) -> Unit)? = null,
    showValue: Boolean = false,
    starSize: Dp = 24.dp,
    filledTint: Color = MaterialTheme.colorScheme.primary,
    emptyTint: Color = MaterialTheme.colorScheme.outlineVariant,
    valueColor: Color = MaterialTheme.colorScheme.onSurface,
    starContentDescription: (Int) -> String = { "$it star" },
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        val filledCount = round(rating).toInt().coerceIn(0, maxStars)
        repeat(maxStars) { index ->
            val starIndex = index + 1
            val filled = starIndex <= filledCount
            var starModifier = Modifier.size(starSize)
            if (onRatingChange != null) {
                starModifier = starModifier.clickable { onRatingChange(starIndex) }
            }
            Icon(
                imageVector = if (filled) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = if (onRatingChange != null) starContentDescription(starIndex) else null,
                tint = if (filled) filledTint else emptyTint,
                modifier = starModifier
            )
        }
        if (showValue) {
            Spacer(modifier = Modifier.width(CoffeeXpTheme.spacing.base))
            Text(
                text = formatRating(rating),
                style = MaterialTheme.typography.titleMedium,
                color = valueColor
            )
        }
    }
}

private fun formatRating(rating: Double): String {
    val roundedTenths = (rating * 10).roundToInt()
    val whole = roundedTenths / 10
    val tenth = abs(roundedTenths % 10)
    return "$whole.$tenth"
}

@Preview
@Composable
private fun StarRatingReadOnlyPreview() {
    CoffeeXpTheme {
        StarRating(rating = 4.5, showValue = true)
    }
}

@Preview
@Composable
private fun StarRatingInteractivePreview() {
    CoffeeXpTheme {
        StarRating(rating = 4.0, onRatingChange = {})
    }
}
