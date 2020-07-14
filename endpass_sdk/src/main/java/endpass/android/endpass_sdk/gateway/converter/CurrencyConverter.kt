package endpass.android.endpass_sdk.gateway.converter

import androidx.room.TypeConverter
import endpass.android.endpass_sdk.gateway.entity.Currency

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurrencyConverter {

    @TypeConverter
        fun stringToCurrencies(json: String): List<Currency>? {
        val gson = Gson()
        val type = object : TypeToken<List<Currency>>() {

        }.type
        return gson.fromJson<List<Currency>>(json, type)
    }

    @TypeConverter
    fun currencyItemsToString(list: List<Currency>): String {
        val gson = Gson()
        val type = object : TypeToken<List<Currency>>() {

        }.type
        return gson.toJson(list, type)
    }
}