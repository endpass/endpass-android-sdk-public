package endpass.android.endpass_sdk.presentation.ui.auth.login

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.ui.auth.reg.RegistrationFragment
import endpass.android.endpass_sdk.gateway.EnumCollections.LoginScenario.*

private val TAB_TITLES = arrayOf(
    R.string.tab_login_1,
    R.string.tab_login_2
)

class LoginPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment = LoginFragment.newInstance(LOGIN.screen)
        when (position) {
            0 -> fragment = LoginFragment.newInstance(LOGIN.screen)
            1 -> fragment = RegistrationFragment.newInstance()
        }
        return fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return TAB_TITLES.size
    }
}