package endpass.android.endpass_sdk.presentation.ui.dialog

import android.content.Context
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import endpass.android.endpass_sdk.R
import kotlinx.android.synthetic.main.dialog_add_document.*


class AddDocumentDialog(
    context: Context,
    defStyleAttr: Int = R.style.AppBottomSheetDialogTheme
) : BaseBottomSheetDialog(context, defStyleAttr) {

    private var mBehavior: BottomSheetBehavior<View>

    init {
        val layoutBottomSheet = layoutInflater.inflate(R.layout.dialog_add_document, null)
        setContentView(layoutBottomSheet)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        mBehavior = BottomSheetBehavior.from(layoutBottomSheet.parent as View)
    }

    fun setListeners(callback: (Boolean) -> Unit) {

        takePhotoView.setOnClickListener {
            callback.invoke(true)
        }

        fromGalleryView.setOnClickListener {
            callback.invoke(false)
        }

        cancelView.setOnClickListener {
            this.cancel()
        }

    }

}