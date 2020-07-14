package endpass.android.endpass_sdk.presentation.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import java.io.File

fun rotateImage(source: Bitmap, angle: Float): Bitmap {
    val matrix = Matrix()
    matrix.postRotate(angle)
    return Bitmap.createBitmap(
        source, 0, 0, source.width, source.height,
        matrix, true
    )
}

fun getBitmapFromPath(path: String): Bitmap? {

    val imgFile = File(path)

    if (imgFile.exists()) {

        val matrix = Matrix()
        matrix.postRotate(180f)
        val scaledBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)

        val rotatedBitmap = Bitmap.createBitmap(
            scaledBitmap,
            0,
            0,
            scaledBitmap.width,
            scaledBitmap.height,
            matrix,
            true
        )

        return rotatedBitmap

    }
    return null
}