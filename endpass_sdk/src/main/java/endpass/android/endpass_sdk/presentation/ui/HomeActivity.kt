package endpass.android.endpass_sdk.presentation.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.presentation.ui.home.HomeFragment
import endpass.android.endpass_sdk.presentation.utils.LocalData
import endpass.android.endpass_sdk.gateway.entity.login.LoginExtraData
import endpass.android.endpass_sdk.presentation.router.MainRouter
import endpass.android.endpass_sdk.presentation.utils.AppSingleton
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.view_base_toolbar.*
import org.koin.android.ext.android.inject


class HomeActivity : AppCompatActivity() {

    companion object {

        const val EXTRA_CURRENT_SCREEN = "current_screen"
        const val HOME = 0
        const val REQUIRED_DOCUMENT = 1

        fun getStartIntent(context: Context, screen: Int, loginExtraData: LoginExtraData?): Intent {
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(EXTRA_CURRENT_SCREEN, screen)
            return intent
        }
    }

    private var currentScreen = HOME
    private val router by inject<MainRouter>()
    private val localData by inject<LocalData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUp()
        exitIv.setOnClickListener {
            router.openLogin(this)
        }
    }

    private fun setUp() {
        currentScreen = intent.getIntExtra(EXTRA_CURRENT_SCREEN, HOME)
        if (currentScreen == REQUIRED_DOCUMENT) {
            AppSingleton.isOauthFlow = true
        }

        mToolBar.setToolbarPram(getString(R.string.home_toolbar_title))

        if (isOauthFlow()) {
            mToolBar.setToolbarPram(getString(R.string.requested_documents_toolbar_title))
        }
        exitIv.setVisibility(!isOauthFlow())

        switchFragment(currentScreen)
    }

    private fun switchFragment(position: Int) {
        supportFragmentManager.replaceOnce(R.id.frame_container, position.toString(), {
            when (position) {
                HOME -> HomeFragment.newInstance()
                else -> HomeFragment.newInstance()
            }
        }).commit()
    }


    fun isOauthFlow() = AppSingleton.isOauthFlow


}
