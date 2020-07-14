package endpass.android.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import endpass.android.endpass_sdk.presentation.base.Constant
import endpass.android.endpass_sdk.presentation.ext.toast
import endpass.android.endpass_sdk.presentation.ui.result.EndpassManager
import endpass.android.endpass_sdk.presentation.ui.result.EndpassManager.ENDPASS_REQ_RESPONSE
import endpass.android.endpass_sdk.presentation.utils.DateUtils
import endpass.android.endpass_sdk.presentation.utils.LocalData
import kotlinx.android.synthetic.main.fragment_test.*


class TestFragment : androidx.fragment.app.Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setListeners()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == EndpassManager.ENDPASS_REQUEST_CODE) {
                val response = data?.getStringExtra(ENDPASS_REQ_RESPONSE)
                responseTv.text = response
            }
        }

    }

    private fun setListeners() {

        EndpassManager.init("your app id", context!!)

        requestBtn.setOnClickListener {
            EndpassManager.request("scopes",this)
        }
    }


}