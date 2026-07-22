package com.cso.coffeexp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.core.design_system.theme.PillShape
import com.cso.coffeexp.domain.mock.mockCoffeeList
import com.cso.coffeexp.domain.model.Coffee
import org.jetbrains.compose.resources.stringResource
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.cd_coffee_rating
import kotlin.math.abs
import kotlin.math.round

@Composable
fun CoffeeCard(
    modifier: Modifier = Modifier,
    coffee: Coffee,
    onClick: (Coffee) -> Unit = {},
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(coffee) },
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(256.dp)
            ) {
                AsyncImage(
                    model = coffee.imageUrl,
                    contentDescription = coffee.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                RatingBadge(
                    rating = coffee.rating,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(CoffeeXpTheme.spacing.base)
                )
            }

            Column(modifier = Modifier.padding(CoffeeXpTheme.spacing.gutter)) {
                Text(
                    text = coffee.name,
                    style = MaterialTheme.typography.headlineSmall
                )
                if (!coffee.notes.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(CoffeeXpTheme.spacing.base))
                    Text(
                        text = coffee.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun RatingBadge(
    rating: Double,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clip(PillShape)
            .background(Color.Black.copy(alpha = 0.55f))
            .padding(horizontal = CoffeeXpTheme.spacing.base, vertical = CoffeeXpTheme.spacing.base / 2),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = stringResource(Res.string.cd_coffee_rating),
            tint = Color.White,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = formatStarRating(rating),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}

// coffee.rating is 0-10; the badge shows the derived 0-5 star value. Avoids
// String.format, which isn't available in commonMain (JVM-only stdlib API).
private fun formatStarRating(rating: Double): String {
    val roundedTenths = round(rating / 2 * 10).toInt()
    val whole = roundedTenths / 10
    val tenth = abs(roundedTenths % 10)
    return "$whole.$tenth"
}

@Preview(showBackground = true)
@Composable
private fun CoffeeCardPreview() {
    CoffeeXpTheme {
        CoffeeCard(
            coffee = mockCoffeeList.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
