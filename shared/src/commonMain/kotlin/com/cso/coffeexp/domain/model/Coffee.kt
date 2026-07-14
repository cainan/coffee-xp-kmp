package com.cso.coffeexp.domain.model

data class Coffee(
    var id: Long? = null, // Unique ID
    var name: String = "",
    var method: String = "",
    var grade: Float = 10f, // Assuming a numerical grade
    var imageUrl: String? = null, // Optional image URL
    var notes: String? = null, // Optional tasting notes
)