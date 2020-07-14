package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import endpass.android.endpass_sdk.R
import kotlinx.android.synthetic.main.view_base_toolbar.view.*

class CenteredTitleToolbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_base_toolbar, this)
    }

    fun setToolbarPram(title: String) {
        toolbarTitle.text = title
    }
}