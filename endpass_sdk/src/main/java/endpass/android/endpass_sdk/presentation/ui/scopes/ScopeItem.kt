package endpass.android.endpass_sdk.presentation.ui.scopes

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.item_document.view.*
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.utils.DataParser


class ScopeItem @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.item_scope, this)
    }

    fun setData(scopeKey: String) {
        Log.d("scopeKey",scopeKey)
        titleTv.text = DataParser.getLocalizeScope(scopeKey)
    }

}