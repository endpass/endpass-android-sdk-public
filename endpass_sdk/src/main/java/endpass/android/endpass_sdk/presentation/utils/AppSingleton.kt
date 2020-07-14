package endpass.android.endpass_sdk.presentation.utils

import endpass.android.endpass_sdk.gateway.EnumCollections

object AppSingleton {

    var clientId: String = "ad15834f-5b6d-4228-96d6-c5f20422ded4"

    var codeChallengeMethod = "S256"

    var responseType = "code"

    var scope =
        "documents:driver_license:status:read documents:id_card:status:read documents:passport:status:read documents:proof_address:status:read"

    var codeVerifier: String? = null

    var codeChallenge: String? = null

    var challenge: String? = null


    var isOauthFlow = false


    var filters: List<EnumCollections.DocumentType>? = null


}