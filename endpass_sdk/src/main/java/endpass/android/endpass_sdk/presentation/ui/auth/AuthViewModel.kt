package endpass.android.endpass_sdk.presentation.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import endpass.android.endpass_sdk.presentation.vo.Resource
import endpass.android.endpass_sdk.data.exception.handleError
import endpass.android.endpass_sdk.domain.interactor.*
import endpass.android.endpass_sdk.gateway.EnumCollections.*
import endpass.android.endpass_sdk.gateway.entity.login.LoginExtraData
import endpass.android.endpass_sdk.presentation.utils.NotifyUtils

/**
 *                 login()
 *                   |
 *                 check()
 *                   |
 *         :true_ _ _ _ __:false
 *           |               |
 *     requestCode()    resetPassword()
 *           |               |
 *           |            signUp()
 *           |                |
 *           |           requestCode()
 *           |                |
 *           |_ _ _ _ _ _ _ _ |
 *                   |
 *                   |
 *             verifyEmail()
 *
 */


open class AuthViewModel(
    private val loginUseCase: LoginUseCase,
    private val checkPasswordUseCase: CheckPasswordUseCase,
    private val requestCodeUseCase: RequestCodeUseCase,
    private val verifyEmailUseCase: VerifyEmailUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val confirmPasswordUseCase: ConfirmPasswordUseCase,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val recoverUseCase: RecoverUseCase,
    private val recoverConfirmUseCase: RecoverConfirmUseCase,
    private val deleteDocumentUseCase: DeleteDocumentUseCase,
    application: Application
) : AndroidViewModel(application) {


    var loginLiveData: MutableLiveData<Resource<LoginScenario>> = MutableLiveData()
    var token: String? = null
    var loginExtraData = LoginExtraData()
    private val context = getApplication<Application>().applicationContext


    fun login(email: String) {
        loginLiveData.value = Resource.loading(null)
        loginUseCase.execute(
            { response ->
                if (response.success) {
                    check(email)
                    loginExtraData.challengeType = response.challenge.challengeType
                } else {
                    loginLiveData.value = Resource.error(null)
                }
            },
            {
                loginLiveData.value = Resource.error(it.message.toString(), null)

            }, LoginUseCase.Params(email)
        )
    }


    fun requestCode(email: String?, scenario: LoginScenario = LoginScenario.REQUEST_CODE) {
        loginLiveData.value = Resource.loading(null)
        requestCodeUseCase.execute(
            {
                loginLiveData.value = Resource.success(scenario)
                it.code?.let {
                    NotifyUtils.codeNotification(context, it)
                }
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, RequestCodeUseCase.Params(email)
        )
    }


    fun verifyEmail(data: LoginExtraData) {
        if (data.challengeType == ChallengeType.SMS.key) {
            data.challengeType = ChallengeType.OTP.key
        }
        loginLiveData.value = Resource.loading(null)
        verifyEmailUseCase.execute(
            {
                token = it.csrfToken
                loginLiveData.value = Resource.success(LoginScenario.EMAIL_CONFIRMED)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, VerifyEmailUseCase.Params(data.email, data.password, data.challengeType, data.code)
        )
    }


    fun signUp(data: LoginExtraData) {
        loginLiveData.value = Resource.loading(null)
        confirmPasswordUseCase.execute(
            {
                token = it.csrfToken
                loginLiveData.value = Resource.success(LoginScenario.EMAIL_CONFIRMED)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            },
            ConfirmPasswordUseCase.Params(data.email, data.password, data.challengeType, data.code)
        )
    }

    private fun check(email: String) {
        checkPasswordUseCase.execute(
            {
                if (it.hasPassword)
                    loginLiveData.value = Resource.success(LoginScenario.PASSWORD)
            },
            { throwable ->
                handleError(throwable) { _, code ->
                    if (code == Network.EXPECTATION_FAILED.statusCode) {
                        resetPassword(email)
                    }
                }
            }, CheckPasswordUseCase.Params(email)
        )
    }

    private fun resetPassword(email: String) {
        resetPasswordUseCase.execute(
            {
                loginLiveData.value = Resource.success(LoginScenario.REGISTRATION)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, ResetPasswordUseCase.Params(email)
        )
    }

    fun changePassword(
        passwordResetToken: String,
        newPassword: String,
        scenario: LoginScenario = LoginScenario.CHANGE_PASS
    ) {
        loginLiveData.value = Resource.loading(null)
        changePasswordUseCase.execute(
            {
                loginLiveData.value = Resource.success(scenario)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, ChangePasswordUseCase.Params(passwordResetToken, newPassword)
        )
    }


    fun recover(email: String) {
        val scenario = LoginScenario.RECOVER
        loginLiveData.value = Resource.loading(null)
        recoverUseCase.execute(
            {
                loginExtraData.recoverCode = it.message
                loginLiveData.value = Resource.success(scenario)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, RecoverUseCase.Params(email)
        )
    }

    fun confirmRecover(email: String, code: String) {
        val scenario = LoginScenario.RECOVER_CONFIRM
        loginLiveData.value = Resource.loading(null)
        recoverConfirmUseCase.execute(
            {
                loginExtraData.scenario = scenario
                loginLiveData.value = Resource.success(scenario)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, RecoverConfirmUseCase.Params(email, code)
        )
    }

    fun deleteDocument(documentId: String, code: String) {
        loginLiveData.value = Resource.loading(null)
        deleteDocumentUseCase.execute(
            {
                loginLiveData.value = Resource.success(LoginScenario.REMOVE_DOCUMENT)
            },
            {
                loginLiveData.value = Resource.error(error = it)

            }, DeleteDocumentUseCase.Params(documentId, code)
        )
    }
}

