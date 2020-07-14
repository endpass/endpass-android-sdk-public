package endpass.android.endpass_sdk.gateway.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import endpass.android.endpass_sdk.gateway.converter.CurrencyConverter
import endpass.android.endpass_sdk.gateway.entity.Currency

@Database(entities = [Currency::class], version = 1, exportSchema = false)
@TypeConverters(CurrencyConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CurrencyDao

}