@file:Suppress("unused")

package com.vdreamers.vandroid.ext

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

typealias LogErrorAction = (t: Throwable) -> Unit
typealias ScannerCallBack = (path: String, uri: Uri) -> Unit

fun Bitmap.saveImage(
    context: Context,
    imageFileName: String? = null,
    logErrorAction: LogErrorAction? = null,
    scannerCallBack: ScannerCallBack? = null
): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        return saveImage2Media(context, imageFileName, logErrorAction, scannerCallBack)
    }

    try {
        val fileName: String = if (imageFileName == null) {
            "JPEG_${System.currentTimeMillis()}"
        } else {
            "${imageFileName}_${System.currentTimeMillis()}"
        }
        val imageFileAllName = "$fileName.jpg"
        val storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        var success = true
        if (!storageDir.exists()) {
            success = storageDir.mkdirs()
        }
        if (success) {
            val imageFile = File(storageDir, imageFileAllName)
            try {
                val fOut: OutputStream = FileOutputStream(imageFile)
                this.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
                fOut.close()
            } catch (e: Exception) {
                throw IOException("Failed to save bitmap.")
            }

            // Add the image to the system gallery
            addToGalleryExt(context, imageFile.absolutePath, scannerCallBack)
            return true
        } else {
            return false
        }
    } catch (e: Exception) {
        logErrorAction?.invoke(e)
        return false
    }
}

fun Bitmap.saveImage2Media(
    context: Context,
    imageFileName: String? = null,
    logErrorAction: LogErrorAction? = null,
    scannerCallBack: ScannerCallBack? = null
): Boolean {
    val displayName: String = if (imageFileName == null) {
        "JPEG_${System.currentTimeMillis()}"
    } else {
        "${imageFileName}_${System.currentTimeMillis()}"
    }
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val relativeLocation = Environment.DIRECTORY_DCIM
            put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation)
            put(MediaStore.MediaColumns.IS_PENDING, 1)
        }
    }
    val resolver = context.contentResolver
    var uri: Uri? = null
    try {
        uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        uri?.let { itUri ->
            val stream = resolver.openOutputStream(itUri)
            stream?.let { itStream ->
                if (!this.compress(Bitmap.CompressFormat.JPEG, 100, itStream)) {
                    throw IOException("Failed to save bitmap.")
                }
            } ?: throw IOException("Failed to get output stream.")
        } ?: throw IOException("Failed to create new MediaStore record")
        return true
    } catch (e: Exception) {
        uri?.let { itUri ->
            resolver.delete(itUri, null, null)
        }
        logErrorAction?.invoke(e)
        return false
    } finally {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
        }

        // Add the image to the system gallery
        uri?.let { itUri ->
            addToGalleryExt(context, itUri.path, scannerCallBack)
        }
    }
}

fun addToGalleryExt(
    context: Context,
    saveImagePath: String? = null,
    scannerCallBack: ScannerCallBack? = null
) {
    MediaScannerConnection.scanFile(
        context,
        arrayOf(saveImagePath),
        arrayOf("image/jpeg")
    ) { path, uri ->
        scannerCallBack?.invoke(path, uri)
    }
}
