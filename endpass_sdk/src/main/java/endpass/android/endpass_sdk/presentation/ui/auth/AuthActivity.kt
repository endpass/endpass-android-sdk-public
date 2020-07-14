package endpass.android.endpass_sdk.presentation.ui.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.BaseActivity
import endpass.android.endpass_sdk.presentation.ui.auth.login.LoginPagerAdapter
import kotlinx.android.synthetic.main.activity_login.*


class AuthActivity : BaseActivity() {

    companion object {

        const val EXTRA_FLOW_TYPE = "extra_flow_type"

        fun getStartIntent(context: Context, flow: Int): Intent {
            val intent = Intent(context, AuthActivity::class.java)
            intent.putExtra(EXTRA_FLOW_TYPE, flow)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setUp()
    }

    private fun setUp() {
        setupViewPager()
        //setToolbarTitle(currentScreen)
    }

    private fun setupViewPager() {
        val sectionsPagerAdapter =
            LoginPagerAdapter(this, supportFragmentManager)
        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 2
        tabs.setupWithViewPager(viewPager)
    }

}
