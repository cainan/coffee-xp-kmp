package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme

enum class CoffeeXpMessageType {
    Error,
}

@Composable
fun CoffeeXpMessageBanner(
    message: String,
    modifier: Modifier = Modifier,
    type: CoffeeXpMessageType = CoffeeXpMessageType.Error,
) {
    val visual = type.visuals()

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = visual.containerColor,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(CoffeeXpTheme.spacing.gutter),
        horizontalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackSm),
        verticalAlignment = Alignment.Top,
    ) {
        Icon(
            imageVector = visual.icon,
            contentDescription = null,
            tint = visual.contentColor,
        )
        Text(
            text = message,
            color = visual.contentColor,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )
    }
}

private data class CoffeeXpMessageVisuals(
    val containerColor: Color,
    val contentColor: Color,
    val icon: ImageVector,
)

@Composable
private fun CoffeeXpMessageType.visuals(): CoffeeXpMessageVisuals = when (this) {
    CoffeeXpMessageType.Error -> CoffeeXpMessageVisuals(
        containerColor = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        icon = Icons.Filled.ErrorOutline,
    )
}

@Preview
@Composable
private fun CoffeeXpMessageBannerLightPreview() {
    CoffeeXpTheme(darkTheme = false) {
        CoffeeXpMessageBanner(
            message = "We couldn't save your journal entry. Your information is still here, so please try again.",
        )
    }
}

@Preview
@Composable
private fun CoffeeXpMessageBannerDarkPreview() {
    CoffeeXpTheme(darkTheme = true) {
        CoffeeXpMessageBanner(
            message = "We couldn't save your journal entry. Your information is still here, so please try again.",
        )
    }
}
