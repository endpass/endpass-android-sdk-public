package endpass.android.endpass_sdk.presentation.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ext.replaceOnce


class OauthActivity : AppCompatActivity() {


    companion object {
        fun getStartIntent(context: Context): Intent {
            val intent = Intent(context, OauthActivity::class.java)
            return intent
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_oauth)
        switchFragment(0)
    }


    private fun switchFragment(position: Int) {
        supportFragmentManager.replaceOnce(R.id.frame_container, position.toString(), {
            OauthFragment()
        }).commit()
    }


}
