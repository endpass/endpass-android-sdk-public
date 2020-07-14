package endpass.android.endpass_sdk.presentation.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.custom.textwatcher.BaseTextWatcher
import endpass.android.endpass_sdk.presentation.ext.focus
import endpass.android.endpass_sdk.presentation.impl.InputFieldStrategy
import kotlinx.android.synthetic.main.view_input_field.view.*


class ValutInputField @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {


    private var hintText: String? = null

    init {
        layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        View.inflate(context, R.layout.view_input_field, this)
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextInputField, 0, 0)

        setUpDefaultParam(typedArray)

    }

    fun getValue() = inputEt.text.toString().trim()

    fun setValue(value: String) {
        inputEt.setText(value.trim())
    }

    fun focus() = inputEt.focus()


    fun isValidate() = inputLayout.error == null && inputEt.text?.trim().toString().isNotEmpty()

    /**
     * @param strategy
     * @see PasswordStrategy
     * Adds a TextWatcher with validation logic, error text etc
     */
    fun setValidationStrategy(strategy: InputFieldStrategy, callback: ((Boolean) -> Unit)? = null) {

        val textWatcher = BaseTextWatcher()
        textWatcher.apply {
            setTextInputLayout(inputLayout)
            setStrategy(strategy)
            if (callback != null) {
                setCallBack(callback)
            }

        }

        inputEt.addTextChangedListener(textWatcher)
        inputEt.inputType = strategy.inputType()
    }

    private fun setUpDefaultParam(typedArray: TypedArray) {
        hintText = typedArray.getString(R.styleable.TextInputField_hint)
        hintText?.let { inputLayout.hint = it }
    }


}