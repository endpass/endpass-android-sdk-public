package endpass.android.endpass_sdk.presentation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import kotlinx.android.synthetic.main.item_document.view.*
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.hide
import endpass.android.endpass_sdk.presentation.ext.setVisibility
import endpass.android.endpass_sdk.presentation.utils.DateUtils


class DocumentItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_document, this)
    }

    @SuppressLint("SetTextI18n")
    fun setData(documentHolder: DocumentHolder, isFullView: Boolean) {
        titleTv.text = documentHolder.documentType.title
        stepView.setStatus(documentHolder.documentType)
        statusView.setStatus(documentHolder)
        dateTv.text = ""

        if (documentHolder.hasDocument()) {
            dateTv.text = "Exp. " + DateUtils.getDate(documentHolder.document!!.createdAt)
        }

        descView.setVisibility(isFullView)

    }


    fun hideLine() = line.hide()

}