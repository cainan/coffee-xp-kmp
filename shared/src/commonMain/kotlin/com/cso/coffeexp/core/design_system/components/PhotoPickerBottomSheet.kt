package com.cso.coffeexp.core.design_system.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.photo_picker_choose_gallery
import coffeexp.shared.generated.resources.photo_picker_remove_photo
import coffeexp.shared.generated.resources.photo_picker_take_photo
import coffeexp.shared.generated.resources.photo_picker_title
import com.cso.coffeexp.core.design_system.theme.CoffeeXpTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoPickerBottomSheet(
    onDismissRequest: () -> Unit,
    onTakePhotoClick: () -> Unit,
    onChooseFromGalleryClick: () -> Unit,
    isCameraSupported: Boolean = true,
    onRemovePhotoClick: (() -> Unit)? = null,
    sheetState: SheetState = rememberModalBottomSheetState(),
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = CoffeeXpTheme.spacing.stackLg)
        ) {
            Text(
                text = stringResource(Res.string.photo_picker_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = CoffeeXpTheme.spacing.gutter)
                    .padding(bottom = CoffeeXpTheme.spacing.stackSm)
            )

            if (isCameraSupported) {
                BottomSheetOptionItem(
                    icon = Icons.Filled.PhotoCamera,
                    title = stringResource(Res.string.photo_picker_take_photo),
                    onClick = {
                        onDismissRequest()
                        onTakePhotoClick()
                    }
                )
            }

            BottomSheetOptionItem(
                icon = Icons.Filled.PhotoLibrary,
                title = stringResource(Res.string.photo_picker_choose_gallery),
                onClick = {
                    onDismissRequest()
                    onChooseFromGalleryClick()
                }
            )

            if (onRemovePhotoClick != null) {
                BottomSheetOptionItem(
                    icon = Icons.Filled.Delete,
                    title = stringResource(Res.string.photo_picker_remove_photo),
                    tint = MaterialTheme.colorScheme.error,
                    onClick = {
                        onDismissRequest()
                        onRemovePhotoClick()
                    }
                )
            }
        }
    }
}

@Composable
private fun BottomSheetOptionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                horizontal = CoffeeXpTheme.spacing.gutter,
                vertical = CoffeeXpTheme.spacing.stackSm
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = tint
        )
        Spacer(modifier = Modifier.width(CoffeeXpTheme.spacing.base))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = tint
        )
    }
}
