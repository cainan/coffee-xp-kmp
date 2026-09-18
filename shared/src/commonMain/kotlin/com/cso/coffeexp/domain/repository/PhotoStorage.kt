package com.cso.coffeexp.domain.repository

import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.EmptyResult
import com.cso.coffeexp.core.error_handling.Result

interface PhotoStorage {
    suspend fun savePhoto(bytes: ByteArray): Result<String, DataError.Local>
    suspend fun deletePhoto(path: String): EmptyResult<DataError.Local>
}