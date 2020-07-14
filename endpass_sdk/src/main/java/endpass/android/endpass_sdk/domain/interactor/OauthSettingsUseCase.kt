package endpass.android.endpass_sdk.domain.interactor

import endpass.android.endpass_sdk.domain.base.SingleUseCase
import endpass.android.endpass_sdk.domain.repository.LoginRepository
import endpass.android.endpass_sdk.gateway.entity.oauth.OauthSettingsResponse
import io.reactivex.Single
import javax.inject.Inject

class OauthSettingsUseCase @Inject constructor(private val loginRepository: LoginRepository) :
    SingleUseCase<OauthSettingsResponse, OauthSettingsUseCase.Params>() {

    override fun buildUseCaseSingle(params: Params): Single<OauthSettingsResponse> =
        loginRepository.oauthSettings(params)

    data class Params(
        val client_id: String?
    )
}