package endpass.android.endpass_sdk.presentation.ui.document

import android.content.Context
import android.content.Intent
import android.os.Bundle
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.BaseActivity
import endpass.android.endpass_sdk.presentation.ext.replaceOnce
import kotlinx.android.synthetic.main.activity_login.*


class DocumentActivity : BaseActivity() {

    companion object {

        private const val EXTRA_DOCUMENT = "extra_document"
        private const val EXTRA_DOCUMENT_MODE = "extra_document_mode"

        fun getStartIntent(
            context: Context,
            documentHolder: DocumentHolder,
            isTakePhoto: Boolean
        ): Intent {
            val intent = Intent(context, DocumentActivity::class.java)
            intent.putExtra(EXTRA_DOCUMENT, documentHolder)
            intent.putExtra(EXTRA_DOCUMENT_MODE, isTakePhoto)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_document)
        setUp()
        initFragment()
    }

    private fun getDocumentFromExtra() = intent.getParcelableExtra<DocumentHolder>(EXTRA_DOCUMENT)

    private fun isTakePhoto() = intent.getBooleanExtra(EXTRA_DOCUMENT_MODE, true)

    private fun setUp() {
        mToolBar.setToolbarPram(getDocumentFromExtra().documentType.title)
    }

    private fun initFragment(position: Int = 0) {
        supportFragmentManager.replaceOnce(R.id.frame_container, position.toString(), {
            DocumentFragment.newInstance(getDocumentFromExtra(), isTakePhoto())
        }).commit()
    }
}
