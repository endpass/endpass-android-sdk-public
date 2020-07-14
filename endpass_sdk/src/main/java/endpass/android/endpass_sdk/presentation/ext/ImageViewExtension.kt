package endpass.android.endpass_sdk.presentation.ext

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.widget.ImageView
import java.io.File


fun ImageView.loadImageFromStorage(path:String){

    val imgFile = File(path)

    if (imgFile.exists()) {

        val matrix = Matrix()
      //  matrix.postRotate(180f)
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

        setImageBitmap(rotatedBitmap)

    }
}