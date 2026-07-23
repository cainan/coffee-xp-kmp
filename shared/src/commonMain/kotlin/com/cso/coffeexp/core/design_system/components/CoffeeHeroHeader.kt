package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme

@Composable
fun CoffeeHeroHeader(
    imageUrl: String?,
    title: String,
    modifier: Modifier = Modifier,
    eyebrow: String? = null,
    subtitle: String? = null,
    imageContentDescription: String? = null,
    height: Dp = 340.dp,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = imageContentDescription,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        0f to Color.Transparent,
                        0.45f to Color.Transparent,
                        1f to Color.Black.copy(alpha = 0.85f),
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(CoffeeXpTheme.spacing.marginMobile)
        ) {
            if (eyebrow != null) {
                Text(
                    text = eyebrow,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.85f)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
            Text(
                text = title,
                style = MaterialTheme.typography.displaySmall,
                color = Color.White
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontStyle = FontStyle.Italic),
                    color = Color.White.copy(alpha = 0.9f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CoffeeHeroHeaderPreview() {
    CoffeeXpTheme {
        CoffeeHeroHeader(
            imageUrl = "https://picsum.photos/seed/yirgacheffe/400/300",
            eyebrow = "ETHICAL HARVEST SERIES",
            title = "Yirgacheffe Heirloom",
            subtitle = "Rogue Wave Roasters"
        )
    }
}
