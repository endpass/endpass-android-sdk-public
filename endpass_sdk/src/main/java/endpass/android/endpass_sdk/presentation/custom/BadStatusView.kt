package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import endpass.android.endpass_sdk.R
import android.text.Html
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import kotlinx.android.synthetic.main.view_bad_status.view.*
import net.cachapa.expandablelayout.ExpandableLayout


class BadStatusView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var expandableLayout: ExpandableLayout
    private lateinit var callback: (Document?) -> Unit
    private var document: Document? = null


    init {
        View.inflate(context, R.layout.view_bad_status, this)
        setListeners()
    }

    fun setCallback(callback: (Document?) -> Unit) {
        this.callback = callback
    }

    fun setExpandableLayout(expandableLayout: ExpandableLayout) {
        this.expandableLayout = expandableLayout
    }

    fun showView(document: Document?) {
        this.document = document
        expandableItem(true)
        document?.let {
            val sourceString =
                "The <b>" + it.documentType.title + "</b> has the status <b>" + it.status.desc + "</b> , please upload it again"
            errorTv.text = Html.fromHtml(sourceString)
        }
    }

    fun hideView() {
        expandableItem(false)
    }

    private fun expandableItem(isChecked: Boolean) =
        if (isChecked) expandableLayout.expand() else expandableLayout.collapse()


    private fun setListeners() {

        hideTv.setOnClickListener {
            hideView()
        }

        reuploadTv.setOnClickListener {
            callback.invoke(document)
        }

    }


}