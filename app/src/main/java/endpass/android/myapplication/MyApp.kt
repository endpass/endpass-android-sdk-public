package endpass.android.myapplication
import android.app.Application
import endpass.android.endpass_sdk.App


open  class MyApp : App() {


    override fun onCreate() {
        super.onCreate()

       // startKoin(this, listOf(netModule, archModule, utilModule))
    }

}
