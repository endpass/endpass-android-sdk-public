package endpass.android.endpass_sdk.presentation.base

import endpass.android.endpass_sdk.BuildConfig

object Constant {

    /**
     * Endpoint
     */

    var BASE_URL: String = "https://identity-dev.endpass.com"

    var OAUTH_URL: String = "https://api-dev.endpass.com"

    var API_VERSION: String = "/v1/"


    var DOCUMENT_URL: String = "$BASE_URL/api/v1.1/documents/"


    /**
     * Connection timeout duration
     */
    const val CONNECT_TIMEOUT = (60 * 1000).toLong()
    /**
     * Connection Read timeout duration
     */
    const val READ_TIMEOUT = (60 * 1000).toLong()
    /**
     * Connection write timeout duration
     */
    const val WRITE_TIMEOUT = (60 * 1000).toLong()

    const val DEFAULT_PER_PAGE_COUNT = 10

    const val OTP_FIELDS_COUNT = 6


    /**
     * Request codes
     */

    const val PERMISSION_CAMERA_REQUEST_CODE = 200

    const val TAKE_PHOTO_REQUEST_CODE = 1001

    const val PICK_FROM_GALLERY_REQUEST_CODE = 1002

    const val PERMISSION_GALLERY_REQUEST_CODE = 197

    const val OAUTH_REQUEST_CODE = 1032

    const val VERIFIED_DOCUMENT_CODE = 1870



    /**
     * Other
     */

    const val EXTERNAL_FILES_DIRECTORY_NAME = "photos"

    const val CODE_NOTIFICATION_CHANNEL_ID = "CHANNEL_ID"

    const val UPDATE_DOCUMENT_TIMEOUT = 3000L


}