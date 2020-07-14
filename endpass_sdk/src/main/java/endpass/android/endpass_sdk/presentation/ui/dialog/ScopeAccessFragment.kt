package endpass.android.endpass_sdk.presentation.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.ui.auth.OAuthViewModel
import endpass.android.endpass_sdk.presentation.ui.scopes.ScopeAdapter
import kotlinx.android.synthetic.main.fragment_scopes.*
import org.koin.android.viewmodel.ext.android.sharedViewModel


class ScopeAccessFragment : DialogFragment() {


    companion object {

        fun newInstance(): ScopeAccessFragment {
            return ScopeAccessFragment()

        }
    }

    private lateinit var rootView: View
    private val oAuthViewModel: OAuthViewModel by sharedViewModel(from = { parentFragment!! })
    private lateinit var scopeAdapter: ScopeAdapter
    private var scopes = arrayListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_scopes, container, false)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)


        return rootView
    }

    override fun onResume() {
        super.onResume()
        val params = dialog!!.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog!!.window!!.attributes = params as android.view.WindowManager.LayoutParams
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        setListeners()
        confirmBtn.setDisable(false)
    }

    private fun setListeners() {
        confirmBtn.setOnClickListener {
            oAuthViewModel.postConsent(scopes)
            showProgress()
        }
    }


    private fun observeLiveData() {
        oAuthViewModel.scopesLiveData.observe(this, Observer {
            scopes = it.requested_scope.filter {
                it.contains(':')
            } as ArrayList<String>
            setUpRecyclerView()
        })

    }


    private fun setUpRecyclerView() {
        scopeAdapter = ScopeAdapter(scopes)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = scopeAdapter
    }


    private fun hideProgress() {
        mProgressBar.hide()
        confirmBtn.text = context?.getString(R.string.allow)

    }

    private fun showProgress() {
        mProgressBar.show()
        confirmBtn.clearText()
    }


}