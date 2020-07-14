package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.utils.ShapeUtil

class ValutButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var isActive = false

    init {
        setTextColor(Color.WHITE)
        textAlignment = View.TEXT_ALIGNMENT_CENTER
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ValutButton, 0, 0)
        isActive = typedArray.getBoolean(R.styleable.ValutButton_isActive, false)
        setDisable(!isActive)


    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    fun setDisable(isDisable: Boolean) {
        if (isDisable ) {
            background = ShapeUtil.createRectangle(colorHex = "#eeeeee")
            alpha = 0.8f
            setTextColor(Color.parseColor("#5011161a"))
            return
        }
        alpha = 1f
        background = ShapeUtil.createRectangle()
        setTextColor(Color.parseColor("#ffffff"))
    }


}