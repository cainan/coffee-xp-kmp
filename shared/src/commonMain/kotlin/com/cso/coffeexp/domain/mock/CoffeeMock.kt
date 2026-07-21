package com.cso.coffeexp.domain.mock

import com.cso.coffeexp.domain.model.Coffee
import kotlinx.datetime.LocalDate

val mockCoffee = Coffee(
    id = 1,
    imageUrl = "https://picsum.photos/seed/yirgacheffe/400/300",
    name = "Ethiopian Yirgacheffe",
    roaster = "Onyx Coffee Lab",
    series = "Ethical Harvest Series",
    origin = "Gedeo Zone, Ethiopia",
    process = "Washed",
    elevation = "1,900-2,200 MASL",
    roastDate = LocalDate(2026, 6, 12),
    roastLevel = "Light",
    brewingMethod = "V60 Pour-over",
    grindSize = 18,
    temperature = 94,
    ratio = "1:16",
    brewTime = "3:15 min",
    rating = 9.6,
    notes = "Bright acidity with notes of blueberry and jasmine."
)

val mockCoffeeList = listOf(
    mockCoffee,
    Coffee(
        id = 2,
        imageUrl = "https://picsum.photos/seed/supremo/400/300",
        name = "Colombian Supremo",
        roaster = "Counter Culture Coffee",
        origin = "Huila, Colombia",
        process = "Washed",
        roastDate = LocalDate(2026, 5, 28),
        roastLevel = "Medium",
        brewingMethod = "French Press",
        ratio = "1:15",
        brewTime = "4:00 min",
        rating = 9.0,
        notes = "Balanced with caramel sweetness and a nutty finish."
    ),
    Coffee(
        id = 3,
        name = "Brazilian Santos",
        roastLevel = "Dark",
        brewingMethod = "Espresso",
        rating = 8.4
    )
)
