package com.cso.coffeexp.core.utils

import coffeexp.shared.generated.resources.Res
import coffeexp.shared.generated.resources.error_connection
import coffeexp.shared.generated.resources.error_disk_full
import coffeexp.shared.generated.resources.error_not_found
import coffeexp.shared.generated.resources.error_request_timeout
import coffeexp.shared.generated.resources.error_server
import coffeexp.shared.generated.resources.error_unauthorized
import coffeexp.shared.generated.resources.error_unknown
import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.design_system.utils.UiText

fun DataError.toUiText(): UiText = UiText.Resource(
    id = when (this) {
        DataError.Local.DISK_FULL -> Res.string.error_disk_full
        DataError.Local.NOT_FOUND,
        DataError.Remote.NOT_FOUND -> Res.string.error_not_found

        DataError.Connection.NOT_CONNECTED,
        DataError.Connection.MESSAGE_SEND_FAILED,
        DataError.Remote.NO_INTERNET -> Res.string.error_connection

        DataError.Remote.REQUEST_TIMEOUT -> Res.string.error_request_timeout
        DataError.Remote.UNAUTHORIZED,
        DataError.Remote.FORBIDDEN -> Res.string.error_unauthorized

        DataError.Remote.SERVER_ERROR,
        DataError.Remote.SERVICE_UNAVAILABLE -> Res.string.error_server

        DataError.Local.UNKNOWN,
        DataError.Remote.BAD_REQUEST,
        DataError.Remote.CONFLICT,
        DataError.Remote.TOO_MANY_REQUESTS,
        DataError.Remote.PAYLOAD_TOO_LARGE,
        DataError.Remote.SERIALIZATION,
        DataError.Remote.UNKNOWN -> Res.string.error_unknown
    }
)
