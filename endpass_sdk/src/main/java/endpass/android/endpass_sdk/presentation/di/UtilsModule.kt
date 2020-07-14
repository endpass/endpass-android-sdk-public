package endpass.android.endpass_sdk.presentation.di


import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.utils.LocalData
import endpass.android.endpass_sdk.presentation.utils.NetworkHandler

import org.koin.dsl.module.module

val utilModule = module {
    single {
        MainRouter()
    }
    single {
        NetworkHandler(get())
    }
    single {
        LocalData(get())
    }
}