package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import endpass.android.endpass_sdk.gateway.entity.Currency
import kotlinx.android.synthetic.main.view_currency.view.*
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.showPrice


class CurrencyView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    init {
        layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT)
        View.inflate(context, R.layout.view_currency, this)
    }

    fun setData(currency: Currency) {

        priceTv.text = currency.price_usd
        titleTv.text = currency.name
        descTv.text = currency.symbol


        currency.price_usd?.let {
            val price = it.toDouble()
            priceTv.showPrice(price = price)
        }
    }

}