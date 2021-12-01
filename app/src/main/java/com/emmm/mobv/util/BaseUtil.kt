package com.emmm.mobv.util

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.emmm.mobv.R

class BaseUtil {
    companion object {

        @JvmStatic
        fun convertColorToDrawable(income: Boolean, color: Int, color2: Int): ColorDrawable? {
            return if (income) ColorDrawable(color) else ColorDrawable(color2)
        }

        @JvmStatic
        fun getIconIdForIncome(income: Boolean): Int {
            return if (income) R.drawable.plusicon else R.drawable.minusicon
        }

        @BindingAdapter("imgRes")
        @JvmStatic
        fun setImageResource(imageView: ImageView, resource: Int) {
            imageView.setImageResource(resource)
        }
    }
}