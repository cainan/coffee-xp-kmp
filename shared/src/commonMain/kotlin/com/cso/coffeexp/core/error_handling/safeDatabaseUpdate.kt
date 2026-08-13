package com.cso.coffeexp.core.error_handling

import androidx.sqlite.SQLiteException

suspend inline fun <T> safeDatabaseUpdate(update: suspend () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(update())
    } catch (_: SQLiteException) {
        Result.Failure(DataError.Local.DISK_FULL)
    }
}