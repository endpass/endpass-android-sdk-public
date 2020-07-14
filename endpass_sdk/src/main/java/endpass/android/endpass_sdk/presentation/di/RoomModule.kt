package endpass.android.endpass_sdk.presentation.di

import android.app.Application
import androidx.room.Room
import org.koin.dsl.module.module
import endpass.android.endpass_sdk.gateway.dao.AppDatabase

private const val DATABASE_NAME = "Ticker.db"

@Volatile
private var INSTANCE: AppDatabase? = null

private val database =  fun(app: Application): AppDatabase {
    if (INSTANCE == null) {
        synchronized(AppDatabase::class.java) {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME).build()
            }
        }
    }
    return INSTANCE!!
}

private val currencyDao =  fun(db: AppDatabase) = db.categoryDao()

internal val roomModule = module {
    single { database(get()) }
    single { currencyDao(get()) }
}
