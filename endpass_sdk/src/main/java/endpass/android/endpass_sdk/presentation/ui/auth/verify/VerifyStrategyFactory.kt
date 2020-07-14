package endpass.android.endpass_sdk.presentation.ui.auth.verify

import endpass.android.endpass_sdk.presentation.impl.*
import endpass.android.endpass_sdk.gateway.EnumCollections


fun createStrategy(
    challengeType: String?,
    strategyKey: Int
): VerifyFragmentStrategy {

    var strategy: VerifyFragmentStrategy = EmailCodeStrategy()

    when {
        strategyKey == EnumCollections.LoginScenario.RECOVER.screen ->
            strategy = RecoverCodeStrategy()
        strategyKey == EnumCollections.LoginScenario.RECOVER_CONFIRM.screen ->
            strategy = RecoverConfirmStrategy()
        challengeType == EnumCollections.ChallengeType.OTP.key -> strategy = OtpCodeStrategy()
        challengeType == EnumCollections.ChallengeType.EMAIL.key -> strategy = EmailCodeStrategy()
        strategyKey == EnumCollections.LoginScenario.REMOVE_DOCUMENT.screen ->
            strategy = RemoveDocumentStrategy()
        strategyKey == EnumCollections.LoginScenario.OAUTH_CONFIRM.screen ->
            strategy = OauthStrategy()
    }

    return strategy

}


