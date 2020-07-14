package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.LoadingFlowData
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.hideGroupViews
import endpass.android.endpass_sdk.presentation.ext.show
import kotlinx.android.synthetic.main.view_loader.view.*


class ValutLoader @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_loader, this)
    }

    fun update(lodFlowData: LoadingFlowData) {

        var percent = uploadPercentTv.tag.toString().toInt() + lodFlowData.percent
        uploadTitleTv.text = lodFlowData.status.name

        if (lodFlowData.status == EnumCollections.DocumentStatusType.PendingReview) {
            percent = 100
            uploadTitleTv.text = context.getString(R.string.pending_text)
            this.hideGroupViews(uploadPercentTv, progressBar2, progressBar3)
            progressBar.show()

        }
        progressBar3.progress = percent
        uploadPercentTv.tag = percent.toString()

        if (percent < 99) {
            uploadPercentTv.text = "$percent%"
        }


    }


}