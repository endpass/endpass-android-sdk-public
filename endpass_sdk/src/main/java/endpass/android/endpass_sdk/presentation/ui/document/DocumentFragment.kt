package endpass.android.endpass_sdk.presentation.ui.document

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import endpass.android.endpass_sdk.gateway.entity.documents.UploadStatusResponse
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.ui.dialog.LoaderFragment
import endpass.android.endpass_sdk.presentation.ui.document.CameraActivity.Companion.EXTRA_PHOTO_IS_FRONT_SIDE
import endpass.android.endpass_sdk.presentation.ui.home.DocFlowViewModel
import endpass.android.endpass_sdk.presentation.utils.AuthInterceptor.Companion.TOKEN_PREFIX
import endpass.android.endpass_sdk.presentation.utils.DataParser
import endpass.android.endpass_sdk.presentation.utils.LocalData
import endpass.android.endpass_sdk.presentation.vo.Status
import kotlinx.android.synthetic.main.fragment_document.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class DocumentFragment : Fragment() {

    private val localData by inject<LocalData>()
    private val mViewModel: DocFlowViewModel by viewModel()
    private val router by inject<MainRouter>()
    private lateinit var loaderFragment: LoaderFragment
    private val pathList = ArrayList<String>()


    companion object {

        const val BUNDLE_DOCUMENT = "bundle_doc"
        const val BUNDLE_MODE_DOCUMENT = "bundle_mode_document"

        fun newInstance(documentHolder: DocumentHolder, isTakePhoto: Boolean): DocumentFragment {
            return DocumentFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_DOCUMENT, documentHolder)
                    putBoolean(BUNDLE_MODE_DOCUMENT, isTakePhoto)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_document, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        if (isDocumentUpload()) {
            mViewModel.getUploadDocumentStatus(getDocumentFromBundle()?.document?.id!!)
            submitBtn.hide()
        } else {
            openCameraOrGalleryScreen(true)
        }
        setListeners()
    }

    private fun openCameraOrGalleryScreen(isFrontSide: Boolean) {
        if (isTakePhoto()) {
            //router.openCameraActivity(this, isFrontSide)
            router.openCropActivity(this, isFrontSide)
        } else {
            router.openCropActivity(this, isFrontSide)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == Constant.TAKE_PHOTO_REQUEST_CODE) {
                val path = data?.getStringExtra(CameraActivity.EXTRA_PHOTO)
                showPhotos(path, data!!.getBooleanExtra(EXTRA_PHOTO_IS_FRONT_SIDE, true))
            }
        }

    }

    private fun showPhotos(path: String?, isFrontSide: Boolean) {
        if (path != null) {
            if (isFrontSide) {
                pathList.add(0, path)
                frontPhotoIv.loadImageFromStorage(path)
                submitBtn.setDisable(false)
                makeBackSideBtn.show()

            } else {
                backPhotoIv.loadImageFromStorage(path)
                pathList.add(1, path)
                makeBackSideBtn.hide()

            }
        }
    }


    private fun isDocumentUpload() = getDocumentFromBundle()!!.hasDocument()

    private fun getDocumentFromBundle() = arguments?.getParcelable<DocumentHolder>(BUNDLE_DOCUMENT)

    private fun isTakePhoto() = arguments!!.getBoolean(BUNDLE_MODE_DOCUMENT)

    private fun observeLiveData() {

        mViewModel.statusDocumentLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> responseHandler(it.data!!)
                Status.ERROR -> toast("Error")
            }
        })


        mViewModel.documentLiveData.observe(this, Observer {
            if (it.status == Status.ERROR) {
                showSnackbar(
                    root_layout,
                    context!!.getString(R.string.error_text),
                    EnumCollections.MessageType.ERROR
                )
            }
        })
    }

    private fun responseHandler(response: UploadStatusResponse) {
        hideProgress()

        frontPhotoIv.loadImage(
            DataParser.createDocumentUrl(getDocumentFromBundle()!!),
            TOKEN_PREFIX + localData.accessToken
        )

        if (response.isBackFileUploaded()) {
            backPhotoIv.loadImage(
                DataParser.createDocumentUrl(getDocumentFromBundle()!!, false),
                TOKEN_PREFIX + localData.accessToken
            )
        }


        makeBackSideBtn.setVisibility(!response.isBackFileUploaded())

    }


    private fun hideProgress() {
        progressBar.hide()
    }

    private fun showProgress() {
        progressBar.show()
    }


    private fun showProgressDialog() {
        loaderFragment = LoaderFragment.newInstance(getDocumentFromBundle()!!)
        childFragmentManager.showDialogFragment(LoaderFragment::class.java.name) {
            loaderFragment
        }
    }

    private fun setListeners() {

        submitBtn.setOnClickListener {
            if (pathList.isNotEmpty()) {
                mViewModel.checkDocument(pathList, getDocumentFromBundle()!!.documentType)
                showProgressDialog()
            }
        }

        makeBackSideBtn.setOnClickListener {
            openCameraOrGalleryScreen(false)
        }

        frontPhotoIv.setOnClickListener {
            //router.openCameraActivity(this)
        }

        backPhotoIv.setOnClickListener {
           // router.openCameraActivity(this, false)
        }


    }


}


