package com.cso.coffeexp.core.error_handling

import androidx.sqlite.SQLiteException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <T> safeDatabaseUpdate(update: suspend () -> T): Result<T, DataError.Local> {
    return try {
        Result.Success(update())
    } catch (e: CancellationException) {
        throw e
    } catch (e: SQLiteException) {
        Result.Failure(sqliteErrorToLocalDataError(e.message))
    } catch (_: Exception) {
        Result.Failure(DataError.Local.UNKNOWN)
    }
}
