package endpass.android.endpass_sdk.presentation.utils

import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import endpass.android.endpass_sdk.R
import endpass.android.endpass_sdk.presentation.base.Constant.CODE_NOTIFICATION_CHANNEL_ID

object NotifyUtils {

    fun codeNotification(context: Context, code: String) {


        val builder = NotificationCompat.Builder(context, CODE_NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.app_logo)
            .setContentTitle("Endpass verification code:")
            .setContentText(code)
            .setColor(Color.WHITE)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)



        with(NotificationManagerCompat.from(context)) {
            notify(1, builder.build())
        }

        VibratorUtils.vibrate(context)
    }

}