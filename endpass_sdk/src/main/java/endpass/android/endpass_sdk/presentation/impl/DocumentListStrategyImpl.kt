package endpass.android.endpass_sdk.presentation.impl


interface DocumentListStrategyImpl {

    fun getTitle(): String

}

class MainDocumentStrategy : DocumentListStrategyImpl {
    override fun getTitle(): String = "ALL UPLOADED DOCUMENTS"
}

class OauthDocumentStrategy : DocumentListStrategyImpl {
    override fun getTitle(): String = "SELECT DOCUMENT"
}