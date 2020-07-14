package endpass.android.endpass_sdk.gateway.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import endpass.android.endpass_sdk.gateway.entity.Currency
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currencyTable order by currency_id")
    fun findAll() : Single<List<Currency>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Currency>):Completable
}
