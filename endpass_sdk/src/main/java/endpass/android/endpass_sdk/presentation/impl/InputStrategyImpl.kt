package endpass.android.endpass_sdk.presentation.impl

import android.text.InputType
import endpass.android.endpass_sdk.presentation.base.Constant.OTP_FIELDS_COUNT
import endpass.android.endpass_sdk.presentation.ext.isValidCode
import endpass.android.endpass_sdk.presentation.ext.isValidEmail


interface InputFieldStrategy {

    fun isValidate(input: String): Boolean

    fun warningMessage(): String

    fun inputType() = InputType.TYPE_CLASS_TEXT
}


class PasswordStrategy : InputFieldStrategy {

    override fun isValidate(input: String): Boolean {
        return input.length > 7
    }

    override fun warningMessage(): String =
        "Password-minimum 8 characters"

    override fun inputType() = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD


}

class EmailStrategy : InputFieldStrategy {

    override fun isValidate(input: String): Boolean = input.trim().isValidEmail()

    override fun warningMessage(): String = "The email field must be a valid email"

}

class CodeStrategy : InputFieldStrategy {

    override fun isValidate(input: String): Boolean = input.isValidCode()

    override fun warningMessage(): String = "Code-length $OTP_FIELDS_COUNT characters"

    override fun inputType() = InputType.TYPE_CLASS_NUMBER


}