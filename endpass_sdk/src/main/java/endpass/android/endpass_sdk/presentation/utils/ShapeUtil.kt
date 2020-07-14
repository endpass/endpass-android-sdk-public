package endpass.android.endpass_sdk.presentation.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable


object ShapeUtil {

    fun createRectangle(cornerRadius:Float = 10f, colorHex:String  = "#7F39DB" ): GradientDrawable {
        val shape = GradientDrawable()
        shape.shape = GradientDrawable.RECTANGLE
        shape.cornerRadius = cornerRadius
        shape.setColor(Color.parseColor(colorHex))
        return shape
    }

}
