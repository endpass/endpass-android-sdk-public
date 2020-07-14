package endpass.android.endpass_sdk.presentation.ui.auth.password

import android.content.Context
import android.content.Intent
import android.os.Bundle
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.BaseActivity
import endpass.android.endpass_sdk.presentation.ext.*
import endpass.android.endpass_sdk.gateway.EnumCollections
import endpass.android.endpass_sdk.gateway.entity.login.LoginExtraData
import kotlinx.android.synthetic.main.activity_home.*


class ChangePassActivity : BaseActivity() {

    companion object {
        private const val EXTRA_LOGIN_DATA = "login_data"


        fun getStartIntent(context: Context, loginExtraData: LoginExtraData?): Intent {
            val intent = Intent(context, ChangePassActivity::class.java)
            intent.putExtra(EXTRA_LOGIN_DATA, loginExtraData)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setUp()
    }

    private fun loginExtraData() = intent.getParcelableExtra<LoginExtraData>(EXTRA_LOGIN_DATA)

    private fun setUp() {
        if (loginExtraData().scenario != null) {
            mToolBar.setToolbarPram(getString(R.string.verify_email))
        } else {
            if (loginExtraData().challengeType == EnumCollections.ChallengeType.OTP.key) {
                mToolBar.setToolbarPram(getString(R.string.disable_otp_text))
            } else {
                mToolBar.setToolbarPram(getString(R.string.change_password))
            }
        }
        switchFragment()
    }

    private fun switchFragment(position: Int = 0) {
        supportFragmentManager.replaceOnce(R.id.frame_container, position.toString(), {
            ChangePassFragment.newInstance(loginExtraData())
        }).commit()
    }


}
