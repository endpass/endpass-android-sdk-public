package endpass.android.endpass_sdk.presentation.ui.dialog

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import endpass.android.endpass_sdk.gateway.entity.documents.LoadingFlowData
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant.UPDATE_DOCUMENT_TIMEOUT
import endpass.android.endpass_sdk.presentation.ext.hide
import endpass.android.endpass_sdk.presentation.ext.show
import endpass.android.endpass_sdk.presentation.ui.document.DocumentFragment
import endpass.android.endpass_sdk.presentation.ui.home.DocFlowViewModel
import endpass.android.endpass_sdk.presentation.utils.AppSingleton
import endpass.android.endpass_sdk.presentation.utils.DataParser
import endpass.android.endpass_sdk.presentation.vo.Status
import kotlinx.android.synthetic.main.fragment_loader.*
import kotlinx.android.synthetic.main.view_success_upload.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class LoaderFragment : DialogFragment() {


    companion object {

        const val BUNDLE_DOCUMENT = "bundle_doc"

        fun newInstance(documentHolder: DocumentHolder?): LoaderFragment {
            return LoaderFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(DocumentFragment.BUNDLE_DOCUMENT, documentHolder)
                }
            }

        }
    }

    private lateinit var rootView: View
    private val docFlowViewModel: DocFlowViewModel by sharedViewModel(from = { parentFragment!! })
    private var isVerified = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_loader, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)


        return rootView
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        continueBtn.setOnClickListener {
            if (isVerified) {
                activity?.setResult(Activity.RESULT_OK)
                activity?.finish()
            } else {
                activity?.onBackPressed()
            }

        }

    }

    private fun getDocumentFromBundle() = arguments?.getParcelable<DocumentHolder>(BUNDLE_DOCUMENT)

    private fun observeLiveData() {

        docFlowViewModel.uploadStateLiveData.observe(this, Observer {

            if (it.status == EnumCollections.DocumentStatusType.PendingReview) {
                docFlowViewModel.getDocuments()
            } else {
                loader.update(it)
            }

        })
        docFlowViewModel.documentLiveData.observe(this, Observer {
            when (it?.status) {
                Status.SUCCESS -> checkDocument(it.data as ArrayList<Document>)
                Status.ERROR -> dismiss()
            }
        })


    }

    private fun checkDocument(documents: ArrayList<Document>) {


        if (getDocumentFromBundle() == null) {
            val loadingFlowData =
                LoadingFlowData(EnumCollections.DocumentStatusType.PendingReview, 100)
            loader.update(loadingFlowData)


            Handler().postDelayed({
                if (DataParser.isSelectedDocumentsHasPositiveStatus()) {
                    isVerified = true
                    if (DataParser.hasPendingReview()) {
                        errorState()
                    } else {
                        successState()
                    }
                } else {
                    isVerified = false
                    errorState()
                }
            }, UPDATE_DOCUMENT_TIMEOUT)


        } else {
            if (AppSingleton.filters != null) {

                DataParser.updateSavedDocuments(documents, getDocumentFromBundle()!!.documentType)


            }
            activity?.onBackPressed()
        }


    }


    private fun successState() {
        loader.hide()
        continueBtn.show()
        continueBtn.setDisable(false)
        successView.show()

        successIv.setImageResource(R.drawable.ic_docs_fine)
        titleTv.text = getString(R.string.congratulations_text)
        descTv.text = getString(R.string.verified_success_text)
    }

    private fun errorState() {

        loader.hide()
        continueBtn.show()
        continueBtn.setDisable(false)
        successView.show()

    }

    private fun simulatePending() {
        Handler().postDelayed({
            docFlowViewModel.getDocuments()
        }, UPDATE_DOCUMENT_TIMEOUT)
    }

}