package com.cso.coffeexp.data.repository

import com.cso.coffeexp.core.error_handling.DataError
import com.cso.coffeexp.core.error_handling.EmptyResult
import com.cso.coffeexp.core.error_handling.Result
import com.cso.coffeexp.core.error_handling.safeFileOperation
import com.cso.coffeexp.domain.repository.PhotoStorage
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.path
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class FileKitPhotoStorage : PhotoStorage {

    private val photosDir: PlatformFile
        get() = FileKit.filesDir / PHOTOS_DIR

    override suspend fun savePhoto(bytes: ByteArray): Result<String, DataError.Local> =
        safeFileOperation {
            withContext(Dispatchers.IO) {

                val compressed = FileKit.compressImage(
                    bytes = bytes,
                    imageFormat = ImageFormat.JPEG,
                    quality = JPEG_QUALITY,
                    maxWidth = MAX_SIZE_PX,
                    maxHeight = MAX_SIZE_PX,
                )

                if (!photosDir.exists()) {
                    photosDir.createDirectories()
                }

                val file = photosDir / "${Clock.System.now().toEpochMilliseconds()}.jpg"

                file.write(compressed)
                file.path
            }
        }

    override suspend fun deletePhoto(path: String): EmptyResult<DataError.Local> =
        safeFileOperation {
            withContext(Dispatchers.IO) {
                val file = PlatformFile(path)
                if (file.exists()) {
                    file.delete()
                }
            }
        }

    private companion object {
        const val PHOTOS_DIR = "coffee_photos"
        const val JPEG_QUALITY = 80
        const val MAX_SIZE_PX = 1600
    }
}