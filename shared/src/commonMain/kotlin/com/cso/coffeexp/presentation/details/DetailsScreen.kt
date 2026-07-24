package com.cso.coffeexp.presentation.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
import androidx.compose.material.icons.filled.Coffee
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.app_name
import coffeexp.shared.generated.resources.cd_back
import coffeexp.shared.generated.resources.cd_edit_coffee
import coffeexp.shared.generated.resources.details_brew_profile
import coffeexp.shared.generated.resources.details_edit_button
import coffeexp.shared.generated.resources.details_elevation_label
import coffeexp.shared.generated.resources.details_final_assessment
import coffeexp.shared.generated.resources.details_grind_size_label
import coffeexp.shared.generated.resources.details_journal_entry
import coffeexp.shared.generated.resources.details_logged_on
import coffeexp.shared.generated.resources.details_method_label
import coffeexp.shared.generated.resources.details_origin_label
import coffeexp.shared.generated.resources.details_process_label
import coffeexp.shared.generated.resources.details_ratio_label
import coffeexp.shared.generated.resources.details_roast_date_label
import coffeexp.shared.generated.resources.details_roast_level_label
import coffeexp.shared.generated.resources.details_temp_label
import coffeexp.shared.generated.resources.details_total_time_label
import coffeexp.shared.generated.resources.details_user_logged_info
import com.cso.coffeexp.core.design_system.components.CoffeeHeroHeader
import com.cso.coffeexp.core.design_system.components.CoffeeXpCard
import com.cso.coffeexp.core.design_system.components.CoffeeXpTopBar
import com.cso.coffeexp.core.design_system.components.InfoRow
import com.cso.coffeexp.core.design_system.components.StarRating
import com.cso.coffeexp.core.design_system.components.StatItem
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.domain.mock.mockCoffee
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DetailsRoot(
    viewModel: DetailsViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
    onEditClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DetailsScreen(
        state = state,
        onAction = { action ->
            when (action) {
                DetailsAction.OnBackClick -> onBackClick()
                DetailsAction.OnEditClick -> onEditClick()
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    state: DetailsState,
    onAction: (DetailsAction) -> Unit,
) {
    val details = state.coffee

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CoffeeXpTopBar(
                title = stringResource(Res.string.app_name),
                onBackClick = { onAction(DetailsAction.OnBackClick) },
                backContentDescription = stringResource(Res.string.cd_back),
                actions = {
                    TextButton(onClick = { onAction(DetailsAction.OnEditClick) }) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = stringResource(Res.string.cd_edit_coffee),
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(stringResource(Res.string.details_edit_button))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            CoffeeHeroHeader(
                imageUrl = details.imageUrl,
                eyebrow = details.series,
                title = details.name,
                subtitle = details.roaster,
                imageContentDescription = details.name
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = CoffeeXpTheme.spacing.marginMobile)
                    .padding(
                        top = CoffeeXpTheme.spacing.stackMd,
                        bottom = CoffeeXpTheme.spacing.stackLg
                    ),
                verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackMd)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackMd)) {
                    Text(
                        text = stringResource(Res.string.details_brew_profile),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    BrewProfileRow(
                        first = Triple(
                            Icons.Filled.Coffee,
                            stringResource(Res.string.details_method_label),
                            details.brewingMethod
                        ),
                        second = Triple(
                            Icons.Filled.WaterDrop,
                            stringResource(Res.string.details_ratio_label),
                            details.ratio ?: "-"
                        )
                    )
                    BrewProfileRow(
                        first = Triple(
                            Icons.Filled.Timer,
                            stringResource(Res.string.details_total_time_label),
                            details.brewTime ?: "-"
                        ),
                        second = Triple(
                            Icons.Filled.Thermostat,
                            stringResource(Res.string.details_temp_label),
                            details.temperature.toString()
                        )
                    )
                    BrewProfileRow(
                        first = Triple(
                            Icons.Filled.Tune,
                            stringResource(Res.string.details_grind_size_label),
                            details.grindSize ?: "-"
                        ),
                        second = Triple(
                            Icons.Filled.LocalFireDepartment,
                            stringResource(Res.string.details_roast_level_label),
                            details.roastLevel
                        )
                    )
                }

                FinalAssessmentCard(
                    rating = details.rating,
                    notes = details.notes ?: ""
                )

                CoffeeXpCard {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Article,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(CoffeeXpTheme.spacing.base))
                        Text(
                            text = stringResource(Res.string.details_user_logged_info),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(
                        modifier = Modifier.padding(top = CoffeeXpTheme.spacing.stackSm),
                        verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackSm)
                    ) {

                        InfoRow(
                            label = stringResource(Res.string.details_origin_label),
                            value = details.origin
                        )

                        InfoRow(
                            label = stringResource(Res.string.details_process_label),
                            value = details.process ?: "-"
                        )

                        InfoRow(
                            label = stringResource(Res.string.details_elevation_label),
                            value = details.elevation ?: "-"
                        )

                        InfoRow(
                            label = stringResource(Res.string.details_roast_date_label),
                            value = details.roastDate.toString()
                        )

                    }
                }

                Text(
                    text = stringResource(Res.string.details_logged_on, details.lastModifiedAt),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun BrewProfileRow(
    first: Triple<ImageVector, String, String>,
    second: Triple<ImageVector, String, String>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(
            icon = first.first,
            label = first.second,
            value = first.third,
            modifier = Modifier.weight(1f)
        )
        StatItem(
            icon = second.first,
            label = second.second,
            value = second.third,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun FinalAssessmentCard(
    rating: Double,
    notes: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.08f),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(CoffeeXpTheme.spacing.base)
                .size(96.dp)
        )
        Column(modifier = Modifier.padding(CoffeeXpTheme.spacing.gutter)) {
            Text(
                text = stringResource(Res.string.details_final_assessment),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(CoffeeXpTheme.spacing.base))
            StarRating(
                rating = rating,
                showValue = true,
                filledTint = MaterialTheme.colorScheme.onPrimary,
                emptyTint = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                valueColor = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(CoffeeXpTheme.spacing.stackSm))
            Text(
                text = stringResource(Res.string.details_journal_entry),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.75f)
            )
            Spacer(modifier = Modifier.height(CoffeeXpTheme.spacing.base))
            Row {
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.4f))
                )
                Spacer(modifier = Modifier.width(CoffeeXpTheme.spacing.base))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CoffeeXpTheme {
        DetailsScreen(
            state = DetailsState(
                coffee = mockCoffee
            ),
            onAction = {}
        )
    }
}
