package com.cso.coffeexp.presentation.new_coffee

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.app_name
import coffeexp.shared.generated.resources.cd_back
import coffeexp.shared.generated.resources.cd_upload_photo
import coffeexp.shared.generated.resources.new_coffee_brewing_method_label
import coffeexp.shared.generated.resources.new_coffee_brewing_specs
import coffeexp.shared.generated.resources.new_coffee_capture_moment_subtitle
import coffeexp.shared.generated.resources.new_coffee_capture_moment_title
import coffeexp.shared.generated.resources.new_coffee_coffee_name_label
import coffeexp.shared.generated.resources.new_coffee_coffee_name_placeholder
import coffeexp.shared.generated.resources.new_coffee_elevation_label
import coffeexp.shared.generated.resources.new_coffee_elevation_placeholder
import coffeexp.shared.generated.resources.new_coffee_grind_size_label
import coffeexp.shared.generated.resources.new_coffee_grind_size_suffix
import coffeexp.shared.generated.resources.new_coffee_notes_placeholder
import coffeexp.shared.generated.resources.new_coffee_origin_label
import coffeexp.shared.generated.resources.new_coffee_origin_placeholder
import coffeexp.shared.generated.resources.new_coffee_overall_rating
import coffeexp.shared.generated.resources.new_coffee_process_label
import coffeexp.shared.generated.resources.new_coffee_process_placeholder
import coffeexp.shared.generated.resources.new_coffee_ratio_label
import coffeexp.shared.generated.resources.new_coffee_roast_date_label
import coffeexp.shared.generated.resources.new_coffee_roast_date_placeholder
import coffeexp.shared.generated.resources.new_coffee_roast_level_label
import coffeexp.shared.generated.resources.new_coffee_roast_level_placeholder
import coffeexp.shared.generated.resources.new_coffee_roaster_label
import coffeexp.shared.generated.resources.new_coffee_roaster_placeholder
import coffeexp.shared.generated.resources.new_coffee_save_button
import coffeexp.shared.generated.resources.new_coffee_series_label
import coffeexp.shared.generated.resources.new_coffee_series_placeholder
import coffeexp.shared.generated.resources.new_coffee_tasting_notes
import coffeexp.shared.generated.resources.new_coffee_temperature_label
import coffeexp.shared.generated.resources.new_coffee_temperature_suffix
import coffeexp.shared.generated.resources.new_coffee_the_beans
import coffeexp.shared.generated.resources.new_coffee_total_time_label
import coffeexp.shared.generated.resources.new_coffee_visual_identity
import com.cso.coffeexp.core.design_system.components.CoffeeXpCard
import com.cso.coffeexp.core.design_system.components.CoffeeXpDropdownField
import com.cso.coffeexp.core.design_system.components.CoffeeXpFilledField
import com.cso.coffeexp.core.design_system.components.CoffeeXpPrimaryButton
import com.cso.coffeexp.core.design_system.components.CoffeeXpTopBar
import com.cso.coffeexp.core.design_system.components.CoffeeXpUnderlinedField
import com.cso.coffeexp.core.design_system.components.PhotoUploadBox
import com.cso.coffeexp.core.design_system.components.SectionHeader
import com.cso.coffeexp.core.design_system.components.StarRating
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun NewCoffeeRoot(
    viewModel: NewCoffeeViewModel = koinViewModel(),
    onBackClick: () -> Unit = {},
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NewCoffeeScreen(
        state = state,
        onAction = { action ->
            when (action) {
                is NewCoffeeAction.OnBackClick -> onBackClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewCoffeeScreen(
    state: NewCoffeeState,
    onAction: (NewCoffeeAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CoffeeXpTopBar(
                title = stringResource(Res.string.app_name),
                onBackClick = { onAction(NewCoffeeAction.OnBackClick) },
                backContentDescription = stringResource(Res.string.cd_back)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = CoffeeXpTheme.spacing.marginMobile)
                .padding(bottom = CoffeeXpTheme.spacing.stackLg),
            verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackMd)
        ) {
            Text(
                text = stringResource(Res.string.new_coffee_visual_identity),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = CoffeeXpTheme.spacing.base)
            )

            PhotoUploadBox(
                title = stringResource(Res.string.new_coffee_capture_moment_title),
                subtitle = stringResource(Res.string.new_coffee_capture_moment_subtitle),
                onClick = { onAction(NewCoffeeAction.OnPhotoClick) },
                photoIconContentDescription = stringResource(Res.string.cd_upload_photo)
            )

            CoffeeXpCard {
                SectionHeader(
                    icon = Icons.Filled.Info,
                    title = stringResource(Res.string.new_coffee_the_beans)
                )
                Column(
                    modifier = Modifier.padding(top = CoffeeXpTheme.spacing.stackSm),
                    verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackSm)
                ) {
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_coffee_name_label),
                        state = state.coffeeNameState,
                        placeholder = stringResource(Res.string.new_coffee_coffee_name_placeholder)
                    )
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_roaster_label),
                        state = state.roasterState,
                        placeholder = stringResource(Res.string.new_coffee_roaster_placeholder)
                    )
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_series_label),
                        state = state.seriesCollectionState,
                        placeholder = stringResource(Res.string.new_coffee_series_placeholder)
                    )
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_origin_label),
                        state = state.originState,
                        placeholder = stringResource(Res.string.new_coffee_origin_placeholder)
                    )
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_process_label),
                        state = state.processState,
                        placeholder = stringResource(Res.string.new_coffee_process_placeholder)
                    )
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_elevation_label),
                        state = state.elevationState,
                        placeholder = stringResource(Res.string.new_coffee_elevation_placeholder)
                    )
                    // TODO: replace with a real M3 DatePickerDialog once the data layer lands.
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_roast_date_label),
                        state = state.roastDateState,
                        placeholder = stringResource(Res.string.new_coffee_roast_date_placeholder)
                    )
                    CoffeeXpUnderlinedField(
                        label = stringResource(Res.string.new_coffee_roast_level_label),
                        state = state.roastLevelState,
                        placeholder = stringResource(Res.string.new_coffee_roast_level_placeholder)
                    )
                }
            }

            CoffeeXpCard {
                SectionHeader(
                    icon = Icons.Filled.HourglassBottom,
                    title = stringResource(Res.string.new_coffee_brewing_specs)
                )
                Column(
                    modifier = Modifier.padding(top = CoffeeXpTheme.spacing.stackSm),
                    verticalArrangement = Arrangement.spacedBy(CoffeeXpTheme.spacing.stackSm)
                ) {
                    CoffeeXpDropdownField(
                        label = stringResource(Res.string.new_coffee_brewing_method_label),
                        selectedValue = state.brewingMethod,
                        options = state.brewingMethodOptions,
                        expanded = state.isBrewingMethodExpanded,
                        onExpandedChange = { onAction(NewCoffeeAction.OnBrewingMethodExpandedChange(it)) },
                        onOptionSelected = { onAction(NewCoffeeAction.OnBrewingMethodSelected(it)) }
                    )
                    CoffeeXpFilledField(
                        label = stringResource(Res.string.new_coffee_grind_size_label),
                        state = state.grindSizeState,
                        suffix = stringResource(Res.string.new_coffee_grind_size_suffix),
                        keyboardType = KeyboardType.Number
                    )
                    CoffeeXpFilledField(
                        label = stringResource(Res.string.new_coffee_temperature_label),
                        state = state.temperatureState,
                        suffix = stringResource(Res.string.new_coffee_temperature_suffix),
                        keyboardType = KeyboardType.Number
                    )
                    CoffeeXpFilledField(
                        label = stringResource(Res.string.new_coffee_ratio_label),
                        state = state.ratioState
                    )
                    CoffeeXpFilledField(
                        label = stringResource(Res.string.new_coffee_total_time_label),
                        state = state.totalTimeState
                    )
                }
            }

            CoffeeXpCard {
                SectionHeader(
                    icon = Icons.Filled.StarOutline,
                    title = stringResource(Res.string.new_coffee_tasting_notes)
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = CoffeeXpTheme.spacing.stackMd),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.new_coffee_overall_rating),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    StarRating(
                        rating = state.overallRating.toDouble(),
                        onRatingChange = { onAction(NewCoffeeAction.OnRatingChange(it)) },
                        starSize = 32.dp,
                        modifier = Modifier.padding(top = CoffeeXpTheme.spacing.base)
                    )
                    CoffeeXpUnderlinedField(
                        label = "",
                        state = state.tastingNotesState,
                        placeholder = stringResource(Res.string.new_coffee_notes_placeholder),
                        lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3),
                        modifier = Modifier.padding(top = CoffeeXpTheme.spacing.stackSm)
                    )
                }
            }

            CoffeeXpPrimaryButton(
                text = stringResource(Res.string.new_coffee_save_button),
                onClick = { onAction(NewCoffeeAction.OnSaveClick) },
                icon = Icons.Filled.Save,
                modifier = Modifier.padding(top = CoffeeXpTheme.spacing.base)
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CoffeeXpTheme {
        NewCoffeeScreen(
            state = NewCoffeeState(
                coffeeNameState = TextFieldState("Ethiopian Yirgacheffe"),
                brewingMethod = "V60 Pour-over",
                grindSizeState = TextFieldState("24"),
                temperatureState = TextFieldState("94"),
                ratioState = TextFieldState("1:16"),
                totalTimeState = TextFieldState("3:15 min"),
                overallRating = 4
            ),
            onAction = {}
        )
    }
}
