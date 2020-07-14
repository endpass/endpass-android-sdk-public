package endpass.android.endpass_sdk.presentation.ext

import android.util.Patterns
import endpass.android.endpass_sdk.presentation.base.Constant.OTP_FIELDS_COUNT

/**
 * Check for valid email extension.
 *
 * @return true if email is valid, false otherwise
 */
fun String.isValidEmail(): Boolean = this.isNotEmpty() &&
        Patterns.EMAIL_ADDRESS.matcher(this).matches()


fun String.isValidPassword(): Boolean {
    let {
        val passwordPattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+\$).{6,}\$"
        val passwordMatcher = Regex(passwordPattern)
        return passwordMatcher.find(this) != null
    }
}

fun String.isValidCode(): Boolean {
    let {
        return it.length == OTP_FIELDS_COUNT
    }
}