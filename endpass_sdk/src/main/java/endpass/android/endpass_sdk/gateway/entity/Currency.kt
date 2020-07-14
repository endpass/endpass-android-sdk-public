package endpass.android.endpass_sdk.gateway.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Keep
@Parcelize
@Entity(tableName = "currencyTable")
data class Currency(

    @PrimaryKey(autoGenerate = true)
    var currency_id: Int = 1,

    val available_supply: String?,
    val id: String?,
    val last_updated: String?,
    val market_cap_usd: String?,
    val max_supply: String?,
    val name: String?,
    val percent_change_1h: String?,
    val percent_change_24h: String?,
    val percent_change_7d: String?,
    val price_btc: String?,
    val price_usd: String?,
    val rank: String?,
    val symbol: String?,
    val total_supply: String?
) : Parcelable