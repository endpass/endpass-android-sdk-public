package endpass.android.endpass_sdk.presentation.ext


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import endpass.android.endpass_sdk.presentation.base.Constant.PERMISSION_CAMERA_REQUEST_CODE


/**
 * Function to check availability of camera permission.
 *
 * @return true if camera permission is granted, false otherwise
 */
val Activity.checkPermission: Boolean
    get() = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED


fun Activity.requestPermission() = ActivityCompat.requestPermissions(
    this,
    arrayOf(Manifest.permission.CAMERA), PERMISSION_CAMERA_REQUEST_CODE
)

fun Activity.setDarkStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.BLACK
    }

}