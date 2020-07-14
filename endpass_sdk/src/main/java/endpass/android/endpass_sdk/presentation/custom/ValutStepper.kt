package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.R
import kotlinx.android.synthetic.main.view_stepper.view.*


class ValutStepper @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_stepper, this)
    }

    fun setStatus(documentType: EnumCollections.DocumentType) {

        circleIv.setImageResource(documentType.logoRes)

    }

}