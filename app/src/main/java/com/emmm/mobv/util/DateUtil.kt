package com.emmm.mobv.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateUtil {
    companion object {

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatDate(timestamp: String): String {
            var tmpTimestamp = timestamp.substring(0, timestamp.indexOf("T"))

            var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            var date = LocalDate.parse(tmpTimestamp, formatter)

            var finaldate = DateTimeFormatter.ofPattern("MMM dd, YYYY");
            return finaldate.format(date).capitalize()
        }
    }
}