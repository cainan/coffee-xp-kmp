package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import com.cso.coffeexp.data.logger.KermitLogger
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoffeeXpDatePickerField(
    label: String,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    minimumDate: LocalDate? = null,
    maximumDate: LocalDate? = null,
    confirmText: String = "OK",
    dismissText: String = "Cancel",
    dateFormatter: (LocalDate) -> String = LocalDate::toDayMonthYear,
) {
    var isDialogVisible by remember { mutableStateOf(false) }
    val minimumDateMillis = minimumDate?.toUtcEpochMilliseconds()
    val maximumDateMillis = maximumDate?.toUtcEpochMilliseconds()
    val selectableDates = remember(minimumDateMillis, maximumDateMillis) {
        DateRangeSelectableDates(
            minimumDateMillis = minimumDateMillis,
            maximumDateMillis = maximumDateMillis,
        )
    }
    val logger: KermitLogger = KermitLogger()
    logger.debug("View displayed")
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedDate?.let(dateFormatter).orEmpty(),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
                readOnly = true,
                placeholder = placeholder?.let { { Text(it) } },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
                ),
            )
            if (enabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            logger.debug("This was clicked")
                            isDialogVisible = true
                        }
                        .semantics { contentDescription = label },
                )
            }
        }
    }

    if (isDialogVisible) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate?.toUtcEpochMilliseconds(),
            selectableDates = selectableDates,
        )
        DatePickerDialog(
            onDismissRequest = { isDialogVisible = false },
            confirmButton = {
                TextButton(
                    enabled = datePickerState.selectedDateMillis != null,
                    onClick = {
                        datePickerState.selectedDateMillis
                            ?.toUtcLocalDate()
                            ?.let(onDateSelected)
                        isDialogVisible = false
                    },
                ) {
                    Text(confirmText)
                }
            },
            dismissButton = {
                TextButton(onClick = { isDialogVisible = false }) {
                    Text(dismissText)
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
private class DateRangeSelectableDates(
    private val minimumDateMillis: Long?,
    private val maximumDateMillis: Long?,
) : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean =
        (minimumDateMillis == null || utcTimeMillis >= minimumDateMillis) &&
                (maximumDateMillis == null || utcTimeMillis <= maximumDateMillis)
}

private fun LocalDate.toUtcEpochMilliseconds(): Long =
    atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

private fun Long.toUtcLocalDate(): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(TimeZone.UTC).date

private fun LocalDate.toDayMonthYear(): String =
    "${day.toString().padStart(2, '0')}/${(month.ordinal + 1).toString().padStart(2, '0')}/$year"

@Preview
@Composable
private fun CoffeeXpDatePickerFieldPreview() {
    CoffeeXpTheme {
        CoffeeXpDatePickerField(
            label = "Roast Date",
            selectedDate = LocalDate(2026, 8, 11),
            onDateSelected = {},
            placeholder = "dd/mm/yyyy",
        )
    }
}
