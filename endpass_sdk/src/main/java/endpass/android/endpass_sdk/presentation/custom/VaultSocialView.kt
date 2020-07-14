package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import androidx.appcompat.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.textAndIconCentered
import kotlinx.android.synthetic.main.view_social_buttons.view.*

class VaultSocialView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : Toolbar(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.view_social_buttons, this)
        initView()
    }

    private fun initView() {
        googleBtn.textAndIconCentered(context.getString(R.string.google), R.drawable.ic_google)
        githubBtn.textAndIconCentered(context.getString(R.string.github), R.drawable.ic_github)
    }

}