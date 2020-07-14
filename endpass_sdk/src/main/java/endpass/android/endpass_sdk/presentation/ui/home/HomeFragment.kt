package endpass.android.endpass_sdk.presentation.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import kotlinx.android.synthetic.main.fragment_home.*
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.OnItemClickListener
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.utils.DataParser
import endpass.android.endpass_sdk.presentation.vo.Status
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import endpass.android.endpass_sdk.presentation.impl.DocumentListStrategyImpl
import endpass.android.endpass_sdk.presentation.impl.MainDocumentStrategy
import endpass.android.endpass_sdk.presentation.impl.OauthDocumentStrategy
import endpass.android.endpass_sdk.presentation.ui.HomeActivity
import endpass.android.endpass_sdk.presentation.ui.auth.AuthViewModel
import endpass.android.endpass_sdk.presentation.ui.dialog.AddDocumentDialog
import endpass.android.endpass_sdk.presentation.ui.dialog.LoaderFragment
import endpass.android.endpass_sdk.presentation.ui.dialog.VerifiedDocumentsDialog
import endpass.android.endpass_sdk.presentation.utils.AppSingleton
import endpass.android.endpass_sdk.presentation.utils.LocalData
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.android.ext.android.inject


class HomeFragment : androidx.fragment.app.Fragment(),
    SwipeRefreshLayout.OnRefreshListener {


    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val mViewModel: DocFlowViewModel by viewModel()
    private val authViewModel: AuthViewModel by viewModel()
    private lateinit var homeAdapter: HomeAdapter
    private var documentHolders = mutableListOf<DocumentHolder>()
    private val router by inject<MainRouter>()
    private val localData by inject<LocalData>()
    private lateinit var loaderFragment: LoaderFragment

    private lateinit var documentListStrategyImpl: DocumentListStrategyImpl


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpStrategy()
        setUpBadStatusView()
        setUpRecyclerView()
        setListeners()
        observeLiveData()
        continueBtn.setVisibility(isOauthFlow())
        extraLoginData().email = localData.email
    }

    private fun setUpStrategy() {
        documentListStrategyImpl = if (isOauthFlow()) {
            OauthDocumentStrategy()
        } else {
            MainDocumentStrategy()
        }
    }

    private fun setUpBadStatusView() {
        badStatusView.setExpandableLayout(expandableView)
        badStatusView.setCallback {
            // showAddDocumentDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        updateStateContinueBtn()
    }

    private fun updateStateContinueBtn() {
        if (DataParser.isSelectedDocumentsHasPositiveStatus()) {
            continueBtn.setDisable(!DataParser.isAllDocumentsSelected())
        } else {
            continueBtn.setDisable(true)
        }
    }


    private fun extraLoginData() = authViewModel.loginExtraData


    private fun setUpRecyclerView() {


        homeAdapter = HomeAdapter(documentHolders, object : OnItemClickListener {
            override fun onItemClicked(position: Int) {


                if (isOauthFlow()) {
                    if (DataParser.getVerifiedDocumentsByDocumentType(documentHolders[position].documentType).isNotEmpty()) {
                        showDocumentsDialog(
                            DataParser.getVerifiedDocumentsByDocumentType(
                                documentHolders[position].documentType
                            ), position
                        )

                    } else {
                        showAddDocumentDialog(position)
                    }

                } else {
                    if (documentHolders[position].allDocuments.size > 0) {

                        showDocumentsDialog(
                            documentHolders[position].allDocuments, position
                        )
                    } else {
                        showAddDocumentDialog(position)

                    }
                }

            }
        }, AppSingleton.isOauthFlow)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = homeAdapter

    }


    private fun showAddDocumentDialog(position: Int) {
        val dialog = AddDocumentDialog(context!!)
        dialog.setListeners { isTakePhoto ->
            documentHolders[position].document = null
            router.openDocument(
                this@HomeFragment,
                documentHolders[position],
                isTakePhoto
            )
            dialog.dismiss()
        }
        dialog.show()
    }


    private fun showDocumentsDialog(documents: List<Document>, position: Int) {
        val dialog = VerifiedDocumentsDialog(context!!)
        dialog.setItemClickListeners {

            if (isOauthFlow()) {
                mergeSelectedDocumentsToHolders()
                homeAdapter.notifyDataSetChanged()
                continueBtn.setDisable(!DataParser.isAllDocumentsSelected())
                dialog.dismiss()
            } else {
                val documentHolder = documentHolders[position]
                documentHolder.document = documents[it]
                router.openDocument(this@HomeFragment, documentHolder)
            }

        }
        dialog.setTitle(documentListStrategyImpl.getTitle())
        dialog.setUploadNewDocumenListeners {
            showAddDocumentDialog(position)
            dialog.dismiss()
        }
        dialog.setUpRecyclerView(documents)
        dialog.show()
    }


    private fun observeLiveData() {
        mViewModel.documentLiveData.observe(this, Observer {
            when (it?.status) {
                Status.LOADING -> showProgress()
                Status.SUCCESS -> {
                    handleDocuments(it.data!!)
                }
                Status.ERROR -> showError("Error")
            }
        })

        mViewModel.requireDocumentsLiveData.observe(this, Observer {
            documentHolders.addAll(DataParser.createDocumentHolders(it))
            AppSingleton.filters = it
            initHolders()
        })

    }

    private fun handleDocuments(documents: List<Document>) {

        if (isOauthFlow()) {
            DataParser.updateDocuments(documents as ArrayList<Document>)
            mergeSelectedDocumentsToHolders()
            if (DataParser.getLastDocumentWithBadStatus() == null) {
                badStatusView.hideView()
            } else {
                badStatusView.showView(DataParser.getLastDocumentWithBadStatus())
            }
            updateStateContinueBtn()


        } else {
            DataParser.mergeDocumentsToHolders(documents, documentHolders)
        }
        homeAdapter.notifyDataSetChanged()
        hideProgress()

    }


    private fun mergeSelectedDocumentsToHolders() {
        DataParser.mergeSelectedDocumentsToHolders(
            DataParser.getSavedDocuments(),
            documentHolders
        )
    }


    private fun loadData() {
        documentHolders.clear()
        if (isOauthFlow()) {
            mViewModel.getRequireDocuments()
        } else {
            documentHolders.addAll(DataParser.createDocumentHolders())
            initHolders()
        }
    }

    private fun initHolders() {
        homeAdapter.notifyDataSetChanged()
        mViewModel.getDocuments()
    }

    private fun isOauthFlow() = (activity as HomeActivity).isOauthFlow()


    private fun setListeners() {
        swipeRefreshLayout.setOnRefreshListener(this)
        swipeRefreshLayout.setOnRefreshListener {
            loadData()
        }

        continueBtn.setOnClickListener {

            if (DataParser.isSelectedDocumentsHasPositiveStatus()) {
                if (!DataParser.hasPendingReview()) {
                    activity?.setResult(Activity.RESULT_OK)
                    activity?.finish()
                }
            } else {
                toast("Choose document")
            }

            showPendingDialog()

        }
    }


    private fun showPendingDialog() {
        loaderFragment = LoaderFragment.newInstance(null)
        childFragmentManager.showDialogFragment(LoaderFragment::class.java.name) {
            loaderFragment
        }
    }

    private fun showError(error: String) {
        showSnackbar(root_layout, error)
    }

    override fun onRefresh() {
        loadData()
    }

    private fun hideProgress() {
        swipeRefreshLayout.isRefreshing = false
    }

    private fun showProgress() {
        swipeRefreshLayout.isRefreshing = true
    }


    private fun openCamera() {
        router.openCameraActivity(this)
    }
}