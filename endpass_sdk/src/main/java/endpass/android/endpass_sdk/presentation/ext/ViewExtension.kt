package endpass.android.endpass_sdk.presentation.ext


import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ImageSpan
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.CurrencyType
import endpass.android.endpass_sdk.presentation.utils.AuthInterceptor.Companion.AUTHORIZATION_TOKEN
import java.lang.StringBuilder
import java.text.NumberFormat
import java.util.*


val Context.networkInfo: NetworkInfo? get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

fun Context.toast(message: CharSequence) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun TextView.showPrice(currency: String = CurrencyType.USD.name, price: Double) {

    val builder = StringBuilder()
    val numberFormat = NumberFormat.getCurrencyInstance()
    numberFormat.currency = Currency.getInstance(currency.toUpperCase())
    val res = numberFormat.format(price)
    builder.append(res)

    text = builder.toString()

}

fun ImageView.loadImage(url: String, token: String) {
    val glideUrl = GlideUrl(
        url,
        LazyHeaders.Builder()
            .addHeader(AUTHORIZATION_TOKEN, token)
            .build()
    )

    Glide
        .with(this.context)
        .load(glideUrl)
        .apply(RequestOptions().placeholder(R.drawable.placeholder))
        .into(this)


}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

fun TextView.customStyle(text: String) {
    setText(Html.fromHtml(text))
}

fun TextView.clearText() {
    text = ""
}

fun View.showGroupViews(vararg view: View) {
    view.forEach {
        it.show()
    }
}

fun View.hideGroupViews(vararg view: View) {
    view.forEach {
        it.hide()
    }
}

fun View.setVisibility(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}


fun Button.textAndIconCentered(title: String, @DrawableRes icon: Int) {
    val buttonLabel = SpannableString("   $title")
    buttonLabel.setSpan(
        ImageSpan(
            context, icon,
            ImageSpan.ALIGN_BOTTOM
        ), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
    )
    text = buttonLabel
}


fun View.hideKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun EditText.focus() {
    this.isFocusableInTouchMode = true
    this.isFocusable = true
    this.requestFocus()
}
