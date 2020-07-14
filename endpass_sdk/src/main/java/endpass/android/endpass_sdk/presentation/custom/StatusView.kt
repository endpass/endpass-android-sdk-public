package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import endpass.android.endpass_sdk.R
import kotlinx.android.synthetic.main.view_status.view.*


class StatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_status, this)
    }

    fun setStatus(documentHolder: DocumentHolder) {

        descTv.text = documentHolder.desc

        if (documentHolder.hasDocument()) {
            descTv.text = documentHolder.document?.status?.desc
            descTv.setTextColor(Color.parseColor(documentHolder.document?.status?.colorHex))
        } else {
            descTv.setTextColor(context.getColor(R.color.colorTextHint))
        }

    }

}