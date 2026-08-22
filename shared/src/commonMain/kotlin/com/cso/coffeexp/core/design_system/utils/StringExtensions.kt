package com.cso.coffeexp.core.design_system.utils

fun String?.orDash(fallback: String = "-"): String {
    return if (this.isNullOrBlank()) fallback else this
}