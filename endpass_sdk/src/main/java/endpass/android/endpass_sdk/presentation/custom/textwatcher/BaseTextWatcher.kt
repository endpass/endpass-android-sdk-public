package endpass.android.endpass_sdk.presentation.custom.textwatcher

import android.text.Editable
import com.google.android.material.textfield.TextInputLayout
import endpass.android.endpass_sdk.presentation.impl.InputFieldStrategy

class BaseTextWatcher : SimpleTextWatcher {

    private lateinit var inputFieldStrategy: InputFieldStrategy
    private lateinit var textInputLayout: TextInputLayout
    private lateinit var callback: (Boolean) -> Unit

    fun setCallBack(callback: (Boolean) -> Unit) {
        this.callback = callback
    }


    fun setStrategy(inputFieldStrategy: InputFieldStrategy) {
        this.inputFieldStrategy = inputFieldStrategy
    }

    fun setTextInputLayout(textInputLayout: TextInputLayout) {
        this.textInputLayout = textInputLayout
    }

    override fun afterTextChanged(s: Editable?) {
        if (s!!.isNotEmpty()) {
            val res = s.toString()
            if (inputFieldStrategy.isValidate(res)) {
                textInputLayout.error = null
                if (::callback.isInitialized) {
                    callback.invoke(true)
                }
            } else {
                if (::callback.isInitialized) {
                    callback.invoke(false)
                }
                textInputLayout.error = inputFieldStrategy.warningMessage()
            }
        }
    }
}