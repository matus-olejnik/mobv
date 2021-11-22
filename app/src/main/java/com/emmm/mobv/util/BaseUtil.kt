package com.emmm.mobv.util

import android.graphics.drawable.ColorDrawable

class BaseUtil {
    companion object {

        @JvmStatic
        fun convertColorToDrawable(income: Boolean, color: Int, color2: Int): ColorDrawable? {
            return if (income) ColorDrawable(color) else ColorDrawable(color2)
        }
    }
}