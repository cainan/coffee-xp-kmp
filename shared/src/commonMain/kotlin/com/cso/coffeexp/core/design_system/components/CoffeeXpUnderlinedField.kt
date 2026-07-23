package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme

@Composable
fun CoffeeXpUnderlinedField(
    label: String,
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextField(
            state = state,
            modifier = Modifier.fillMaxWidth(),
            placeholder = placeholder?.let { { Text(it) } },
            lineLimits = lineLimits,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outlineVariant,
            ),
        )
    }
}

@Preview
@Composable
private fun CoffeeXpUnderlinedFieldPreview() {
    CoffeeXpTheme {
        CoffeeXpUnderlinedField(
            label = "Coffee Name",
            state = rememberTextFieldState(),
            placeholder = "e.g. Ethiopian Yirgacheffe"
        )
    }
}
