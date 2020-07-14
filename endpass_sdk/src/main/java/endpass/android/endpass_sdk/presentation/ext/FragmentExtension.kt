package endpass.android.endpass_sdk.presentation.ext

import androidx.annotation.IdRes
import android.view.View
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import endpass.android.endpass_sdk.gateway.EnumCollections.*
import com.google.android.material.snackbar.Snackbar


inline fun androidx.fragment.app.FragmentManager.replaceOnce(
    @IdRes containerViewId: Int,
    fragmentTag: String,
    body: () -> androidx.fragment.app.Fragment,
    withBackStack: Boolean = false
): androidx.fragment.app.FragmentTransaction {

    val transaction = this.beginTransaction()
    val fragment = this.findFragmentByTag(fragmentTag)
    if (fragment == null) {
        transaction.replace(containerViewId, body(), fragmentTag)
        if (withBackStack) {
            transaction.addToBackStack(fragmentTag)
        }
    }
    return transaction

}

inline fun androidx.fragment.app.FragmentManager.replaceByTag(
    @IdRes containerViewId: Int,
    fragmentTag: String,
    body: () -> androidx.fragment.app.Fragment,
    withBackStack: Boolean = true
): androidx.fragment.app.FragmentTransaction {

    val transaction = this.beginTransaction()
    val fragment = this.findFragmentByTag(fragmentTag)
    if (fragment != null) {
        transaction.replace(containerViewId, fragment, fragmentTag)
    } else {
        transaction.replace(containerViewId, body(), fragmentTag)
    }
    if (withBackStack) {
        transaction.addToBackStack(fragmentTag)
    }
    return transaction
}

fun androidx.fragment.app.Fragment.toast(message: String) =
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()


fun showSnackbar(view: View, message: String, messageType: MessageType = MessageType.SUCCESS) =
    Snackbar
        .make(view, message, Snackbar.LENGTH_LONG)
        .setAction(messageType.actionTitle) { }
        .setActionTextColor(messageType.actionTitleColor)
        .show()


inline fun FragmentManager.showDialogFragment(fragmentTag: String?, body: () -> DialogFragment?) {
    val fragmentTransaction = beginTransaction()
    val prev = findFragmentByTag(fragmentTag)
    if (prev != null) {
        fragmentTransaction.remove(prev)
    }
    body()?.show(fragmentTransaction, fragmentTag)
}