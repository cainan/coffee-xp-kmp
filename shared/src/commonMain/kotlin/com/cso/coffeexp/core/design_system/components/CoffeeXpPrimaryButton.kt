package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme

@Composable
fun CoffeeXpPrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    iconContentDescription: String? = null,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        enabled = enabled,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
        )
    ) {

        Box(
            contentAlignment = Alignment.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(if (isLoading) 0f else 1f)
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = iconContentDescription,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(CoffeeXpTheme.spacing.base))
                }
                Text(text = text, style = MaterialTheme.typography.titleMedium)
            }

            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }

        }
    }

}

@Preview
@Composable
private fun CoffeeXpPrimaryButtonPreview() {
    CoffeeXpTheme {
        CoffeeXpPrimaryButton(
            text = "Save Journal Entry",
            onClick = {},
            icon = Icons.Filled.Save
        )
    }
}

@Preview
@Composable
private fun CoffeeXpPrimaryLoadingButtonPreview() {
    CoffeeXpTheme {
        CoffeeXpPrimaryButton(
            text = "Save Journal Entry",
            onClick = {},
            icon = Icons.Filled.Save,
            isLoading = true
        )
    }
}
