package endpass.android.endpass_sdk.presentation.ui.result

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import endpass.android.endpass_sdk.presentation.ext.toast
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.utils.DateUtils
import endpass.android.endpass_sdk.presentation.utils.LocalData

object EndpassManager {



    const val ENDPASS_CONNECT_REQUEST_CODE = 1802

    const val ENDPASS_REQUEST_CODE = 1902

    const val ENDPASS_REQ_RESPONSE = "response"



    private var router: MainRouter = MainRouter()

    private lateinit var context: Context
    private lateinit var localData: LocalData

    var activity: Activity? = null
    var fragment: Fragment? = null
    var requestType: String = ""
    var clientId: String = ""


    fun init(
        clientId: String,
        context: Context
    ) {

        this.clientId = clientId
        this.context = context
        localData = LocalData(context)
    }


    /**
     * @param requestType Your application id
     * @param fragment You'll get connection result in this
     * fragment using "onActivityResult" method
     */


    fun request(requestType: String, fragment: Fragment) {

        this.fragment = fragment
        this.activity = null

        request(requestType) {
            router.openRequest(requestType, fragment)
        }

    }

    fun request(requestType: String, activity: Activity) {

        this.fragment = null
        this.activity = activity

        request(requestType) {
            router.openRequest(requestType, activity)
        }

    }

    private fun request(requestType: String, callback: () -> Unit) {
        if (isOauth()) {
            callback.invoke()
        } else {
            router.openOauth(context)
        }
        this.requestType = requestType

    }

    /**
     *  @return true
     *  if token exist and difference between createdTokenTime and currentTime less than 1 hour
     */
    private fun isOauth(): Boolean {

        val createdDate = DateUtils.convertLongToDate(localData.oauthTokenCreatedTime)

        return localData.oauthToken != "" && localData.oauthTokenCreatedTime != 0L && DateUtils.differentHourTwoDate(
            createdDate
        ) < 1L

    }


}