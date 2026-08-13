package com.cso.coffeexp.core.error_handling

class DataErrorException(
    val error: DataError
) : Exception()