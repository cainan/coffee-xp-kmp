package com.cso.coffeexp.core.error_handling

import kotlinx.coroutines.test.runTest
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class SafeDatabaseUpdateTest {

    @Test
    fun `successful update returns its value`() = runTest {
        val result = safeDatabaseUpdate { 42L }

        assertEquals(42L, assertIs<Result.Success<Long>>(result).data)
    }

    @Test
    fun `bundled driver full error maps to disk full`() {
        assertEquals(
            DataError.Local.DISK_FULL,
            sqliteErrorToLocalDataError("Error code: 13, message: database or disk is full"),
        )
    }

    @Test
    fun `framework driver full error maps to disk full`() {
        assertEquals(
            DataError.Local.DISK_FULL,
            sqliteErrorToLocalDataError("database or disk is full (code 13 SQLITE_FULL)"),
        )
    }

    @Test
    fun `other sqlite errors map to unknown`() {
        listOf(
            "Error code: 5, message: database is locked",
            "Error code: 21, message: connection is closed",
            // Extended code whose low byte is 19 (constraint), not 13.
            "Error code: 2067, message: UNIQUE constraint failed: coffee.id",
        ).forEach { message ->
            assertEquals(DataError.Local.UNKNOWN, sqliteErrorToLocalDataError(message), message)
        }
    }

    @Test
    fun `messages without an error code map to unknown`() {
        assertEquals(DataError.Local.UNKNOWN, sqliteErrorToLocalDataError(null))
        assertEquals(DataError.Local.UNKNOWN, sqliteErrorToLocalDataError("database or disk is full"))
    }

    @Test
    fun `non sqlite exceptions map to unknown`() = runTest {
        val result = safeDatabaseUpdate<Long> { throw IllegalStateException("Connection pool is closed") }

        assertEquals(DataError.Local.UNKNOWN, assertIs<Result.Failure<DataError.Local>>(result).error)
    }

    @Test
    fun `cancellation is rethrown`() = runTest {
        assertFailsWith<CancellationException> {
            safeDatabaseUpdate<Long> { throw CancellationException("cancelled") }
        }
    }
}
