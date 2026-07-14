package com.cso.coffeexp.core.design_system.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

// Corner-radius scale (specs/DESIGN.md `rounded` tokens):
// sm (4dp) -> extraSmall, DEFAULT (8dp) -> small, md (12dp) -> medium,
// lg (16dp) -> large, xl (24dp) -> extraLarge.
val AppShapes: Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(24.dp),
)

// `rounded.full` (9999px) has no matching Material3 Shapes slot, so it's
// exposed separately. CircleShape = RoundedCornerShape(percent = 50),
// yielding a stadium/pill shape on rectangles and a circle on 1:1 elements.
val PillShape: Shape = CircleShape
