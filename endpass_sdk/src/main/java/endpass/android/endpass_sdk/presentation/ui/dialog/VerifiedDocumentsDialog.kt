package endpass.android.endpass_sdk.presentation.ui.dialog

import android.content.Context
import android.view.View
import endpass.android.endpass_sdk.gateway.entity.documents.Document
import com.google.android.material.bottomsheet.BottomSheetBehavior
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.OnItemClickListener
import endpass.android.endpass_sdk.presentation.ui.home.HomeAdapter
import endpass.android.endpass_sdk.presentation.utils.DataParser
import kotlinx.android.synthetic.main.dialog_verified_documents.*


class VerifiedDocumentsDialog(
    context: Context,
    defStyleAttr: Int = R.style.AppBottomSheetDialogTheme
) : BaseBottomSheetDialog(context, defStyleAttr) {

    private var mBehavior: BottomSheetBehavior<View>
    private lateinit var homeAdapter: HomeAdapter
    private lateinit var clickItemCallback: (Int) -> Unit
    private lateinit var uploadNewDocumentCallback: () -> Unit


    init {
        val layoutBottomSheet = layoutInflater.inflate(R.layout.dialog_verified_documents, null)
        setContentView(layoutBottomSheet)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        mBehavior = BottomSheetBehavior.from(layoutBottomSheet.parent as View)

    }

    fun setTitle(title: String) {
        titleTv.text = title
    }

    fun setItemClickListeners(callback: (Int) -> Unit) {
        this.clickItemCallback = callback
    }

    fun setUploadNewDocumenListeners(callback: () -> Unit) {
        this.uploadNewDocumentCallback = callback
    }

    fun setUpRecyclerView(documents: List<Document>) {

        val documentHolders = DataParser.createVerifiedDocumentHolders(documents)
        homeAdapter =
            HomeAdapter(documentHolders, object : OnItemClickListener {
                override fun onItemClicked(position: Int) {
                    DataParser.selectDocument(documentHolders[position].document!!)
                    clickItemCallback.invoke(position)
                    cancel()
                }
            },true)
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        recyclerView.adapter = homeAdapter
        homeAdapter.notifyDataSetChanged()

        uploadNewDocument.setOnClickListener {
            uploadNewDocumentCallback.invoke()
        }

    }

}