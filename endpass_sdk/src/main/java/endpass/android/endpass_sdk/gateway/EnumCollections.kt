package endpass.android.endpass_sdk.gateway

import android.graphics.Color
import androidx.annotation.Keep
import endpass.android.endpass_sdk.R


@Keep
open class EnumCollections {

    @Keep
    enum class LoginScenario(val screen: Int, val title: String) {
        LOGIN(0, "LOGIN"),
        PASSWORD(1, "PASSWORD"),
        REGISTRATION(2, "REGISTRATION"),
        REQUEST_CODE(3, "VERIFY EMAIL"),
        RESENT_CODE(4, "RESENT CODE"),
        EMAIL_CONFIRMED(5, "EMAIL_CONFIRMED"),
        CHANGE_PASS(6, "CHANGE PASSWORD"),
        RECOVER(7, "RECOVER OTP"),
        RECOVER_CONFIRM(8, "RECOVER_CONFIRM"),
        REMOVE_DOCUMENT(9, "REMOVE_DOCUMENT"),
        OAUTH_CONFIRM(10, "OAUTH_CONFIRM"),
    }

    @Keep
    enum class Network(val statusCode: Int) {
        STATUS_OK(200),
        UNAUTHORIZED(401),
        FORBIDDEN(204),
        HTTP_METHOD_NOT_ALLOWED(405),
        EXPECTATION_FAILED(417)

    }


    @Keep
    enum class ChallengeType(val key: String) {
        OTP("otp"),
        EMAIL("emailLink"),
        SMS("sms")
    }


    @Keep
    enum class DocumentStatusType(val desc: String, val colorHex: String, val isActive: Boolean) {
        Check("Check", "#66000000", false),
        Draft("Draft", "#50565B", false),
        Uploading("Uploading", "#66000000", false),
        Recognition("Recognizing", "#66000000", false),
        NotReadable("Not readable", "#D91D28", false),
        NotVerified("Not verified", "#D91D28", false),
        Verified("Verified", "#24A148", true),
        PendingReview("Pending review", "#FC7B1E", true),
    }

    @Keep
    enum class MessageType(val actionTitle: String, val actionTitleColor: Int) {
        ERROR("ERROR", Color.parseColor("#ff111e")),
        SUCCESS("OK", Color.parseColor("#792bef"))
    }


    @Keep
    enum class UploadFileStatusType {
        Uploaded,
        NoContent
    }

    @Keep
    enum class DocumentType(
        val title: String, val logoRes: Int
    ) {
        Passport("Passport", R.drawable.ic_passport),
        DriverLicense("Driver License", R.drawable.ic_driver_licence),
        ProofOfAddress("Proof of address", R.drawable.ic_location),
        IdCard("Id Card", R.drawable.ic_form),
    }


    @Keep
    enum class AuthFlowType {
        AUTH,
        OAUTH
    }
}