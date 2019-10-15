package com.karthik.trimpark.base.util

import java.text.SimpleDateFormat
import java.util.*


/**
 * created by Karthik A on 2019-08-03
 */
class DateUtil {

    companion object {

        fun getFormattedDate(milliSeconds: Long, format: String): String
        {
            // Create a DateFormatter object for displaying date in specified format.
            val formatter = SimpleDateFormat(format)

            // Create a calendar object that will convert the date and time value in milliseconds to date.
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = milliSeconds

            return formatter.format(calendar.time)
        }
    }
}