package com.cso.coffeexp.core.error_handling

import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeFileOperation(operation: suspend () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(operation())
    } catch (e: CancellationException) {
        throw e
    } catch (_: Exception) {
        Result.Failure(DataError.Local.UNKNOWN)
    }
}
