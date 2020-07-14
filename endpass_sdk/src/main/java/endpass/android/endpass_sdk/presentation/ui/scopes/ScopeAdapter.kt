package endpass.android.endpass_sdk.presentation.ui.scopes

import android.content.Context
import android.view.View
import endpass.android.endpass_sdk.presentation.base.BaseRecyclerAdapter


open class ScopeAdapter(
    dataList: List<String>
) :
    BaseRecyclerAdapter<String>(dataList) {


    override fun getItemView(context: Context, viewType: Int): View {
        return ScopeItem(context)
    }

    override fun bindData(itemView: View, data: String, position: Int) {
        if (itemView is ScopeItem) {
            itemView.setData(data)
        }
    }

}