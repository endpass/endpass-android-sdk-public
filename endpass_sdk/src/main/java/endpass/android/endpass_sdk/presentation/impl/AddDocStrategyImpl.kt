package endpass.android.endpass_sdk.presentation.impl

import android.text.InputType


interface AddDocStrategy {

    fun isValidate(input: String): Boolean

    fun warningMessage(): String

    fun inputType() = InputType.TYPE_CLASS_TEXT
}


