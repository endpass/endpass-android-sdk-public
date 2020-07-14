package endpass.android.endpass_sdk.presentation.ui.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import endpass.android.endpass_sdk.presentation.ext.toast
import endpass.android.endpass_sdk.presentation.ui.home.DocFlowViewModel
import kotlinx.android.synthetic.main.activity_request.*
import org.koin.android.viewmodel.ext.android.viewModel
import okhttp3.*
import java.io.IOException
import com.google.gson.GsonBuilder
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant
import endpass.android.endpass_sdk.presentation.base.Constant.API_VERSION
import endpass.android.endpass_sdk.presentation.base.Constant.OAUTH_URL
import endpass.android.endpass_sdk.presentation.ui.HomeActivity
import endpass.android.endpass_sdk.presentation.ui.result.EndpassManager.ENDPASS_REQ_RESPONSE
import endpass.android.endpass_sdk.presentation.utils.AuthInterceptor.Companion.AUTHORIZATION_TOKEN
import endpass.android.endpass_sdk.presentation.utils.AuthInterceptor.Companion.TOKEN_PREFIX
import endpass.android.endpass_sdk.presentation.utils.DataParser
import endpass.android.endpass_sdk.presentation.utils.LocalData
import org.koin.android.ext.android.inject


class RequestActivity : AppCompatActivity() {


    companion object {

        private val TAG = RequestActivity::class.java.name
        private const val EXTRA_REQUEST_TYPE = "extra_request_type"


        fun getStartIntent(requestType: String, context: Context): Intent {
            val intent = Intent(context, RequestActivity::class.java)
            intent.putExtra(EXTRA_REQUEST_TYPE, requestType)
            return intent
        }
    }


    private val docViewModel: DocFlowViewModel by viewModel()
    private val localData by inject<LocalData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)


        getRequest(getRequestType())

        button.setOnClickListener {
            docViewModel.getRequireDocuments()
        }

        docViewModel.requireDocumentsLiveData.observe(this, Observer {
            getRequest(getRequestType(), it)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            docViewModel.getRequireDocuments()
        }
    }


    private fun getRequestType() = intent.getStringExtra(EXTRA_REQUEST_TYPE)

    private fun getRequest(
        url: String,
        filters: List<EnumCollections.DocumentType>? = null
    ) {


        val okHttpClient = OkHttpClient()
        val request = Request.Builder()
            .header(AUTHORIZATION_TOKEN, TOKEN_PREFIX + localData.oauthToken)
            .url(OAUTH_URL + API_VERSION + url)
            .build()


        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                toast(e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {

                val body = response.body()?.string()
                //   returnResultToSource(body ?: "null")


                this@RequestActivity.runOnUiThread(Runnable {
                    returnResultToSource(body ?: "null")
                })

                /* val gson = GsonBuilder().create()

               val listType = object : TypeToken<List<Document>>() {}.type
                val documents = gson.fromJson<List<Document>>(body, listType)
                this@ResultActivity.runOnUiThread(Runnable {
                    checkDocuments(filters, documents as ArrayList<Document>)
                }

                )*/

                Log.d(TAG, body)

            }
        })

    }

    private fun returnResultToSource(response: String) {
        val result = Intent()
        result.putExtra(ENDPASS_REQ_RESPONSE, response)
        setResult(Activity.RESULT_OK, result)
        finish()
    }

    private fun checkDocuments(
        filters: List<EnumCollections.DocumentType>,
        documents: ArrayList<Document>
    ) {

        val filteredList = documents.filter { it.documentType in filters } as ArrayList<Document>
        val selectedDocuments = DataParser.getSavedDocuments().filter { it.isSelected }
        val filterSelectedDocuments = arrayListOf<Document>()

        filteredList.map { document ->
            selectedDocuments.forEach { selectedDocument ->
                if (document.id == selectedDocument.id) {
                    filterSelectedDocuments.add(document)
                }
            }

        }

        if (DataParser.isAllDocumentsSelected()) {
            showDocuments(filterSelectedDocuments)
        } else {
            startActivityForResult(
                HomeActivity.getStartIntent(this, HomeActivity.REQUIRED_DOCUMENT, null),
                Constant.OAUTH_REQUEST_CODE
            )
        }
    }


    private fun showDocuments(list: ArrayList<Document>) {
        list.forEach {

            resultTv.setSingleLine(false);
            resultTv.append("ID  :  " + it.id + " \n")
            resultTv.append("DocumentType  :  " + it.documentType + " \n")
            resultTv.append("Status  :  " + it.status + " \n")
            resultTv.append(" \n")
            resultTv.append(" \n")

        }
    }


}
