package endpass.android.endpass_sdk.presentation.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*


object DateUtils {

    fun getCurrentTime() = Calendar.getInstance().getTime();


    fun convertLongToDate(milliSeconds: Long) = Date(milliSeconds)


    fun getDate(milliSeconds: Long): String = getDate(milliSeconds, "MMM d, yyyy  ")


    /**
     * Return date in specified format.
     * @param milliSeconds Date in milliseconds
     * @param dateFormat Date format
     * @return String representing date in specified format
     */
    @SuppressLint("SimpleDateFormat")
    fun getDate(milliSeconds: Long, dateFormat: String): String {
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat)

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        calendar.timeInMillis = milliSeconds * 1000L
        return formatter.format(calendar.time)
    }


    fun differentHourTwoDate(comparableDate: Date): Long {

        var different = getCurrentTime().time - comparableDate.time

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        different %= daysInMilli
        return different / hoursInMilli
    }
}