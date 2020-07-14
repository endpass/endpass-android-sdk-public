package endpass.android.endpass_sdk.presentation.ui.home

import android.content.Context
import android.view.View
import endpass.android.endpass_sdk.gateway.entity.DocumentHolder
import endpass.android.endpass_sdk.presentation.base.BaseRecyclerAdapter
import endpass.android.endpass_sdk.presentation.base.OnItemClickListener


open class HomeAdapter(
    dataList: List<DocumentHolder>,
    onItemClickListener: OnItemClickListener,
    var isFullView: Boolean = false
) :
    BaseRecyclerAdapter<DocumentHolder>(dataList, onItemClickListener) {


    override fun getItemView(context: Context, viewType: Int): View {
        return DocumentItem(context)
    }

    override fun bindData(itemView: View, data: DocumentHolder, position: Int) {
        if (itemView is DocumentItem) {
            itemView.setData(data,isFullView)
            if (position == dataList.size - 1) {
                itemView.hideLine()
            }
        }
    }

}