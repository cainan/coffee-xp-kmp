package com.cso.coffeexp.core.error_handling

// SQLiteException carries no error code property, only a message. The bundled driver formats it as
// "Error code: 13, message: ..." and the Android framework driver as "... (code 13 SQLITE_FULL)".
private val SQLITE_CODE_REGEX = Regex("""\bcode:?\s*(\d+)""")

private const val SQLITE_FULL = 13

fun sqliteErrorToLocalDataError(message: String?): DataError.Local {
    val primaryCode = message
        ?.let { SQLITE_CODE_REGEX.find(it)?.groupValues?.get(1)?.toIntOrNull() }
        // Extended result codes (e.g. 2067 SQLITE_CONSTRAINT_UNIQUE) keep the primary code in the low byte.
        ?.and(0xFF)

    return when (primaryCode) {
        SQLITE_FULL -> DataError.Local.DISK_FULL
        else -> DataError.Local.UNKNOWN
    }
}
