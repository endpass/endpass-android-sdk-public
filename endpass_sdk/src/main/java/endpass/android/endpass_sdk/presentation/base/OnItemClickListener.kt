package endpass.android.endpass_sdk.presentation.base

interface OnItemClickListener {

    fun onHeaderClicked() {}

    fun onItemClicked(position: Int)

    fun onFooterClicked() {}
}