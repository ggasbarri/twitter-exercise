package me.gincos.rhoexercise.utils

import android.content.res.Resources
import android.util.TypedValue


/**
 * Convert dp to px value.
 * -
 * Source: https://stackoverflow.com/a/6327095/2263329
 */
fun dpToPx(dp: Float, resources: Resources): Int {
    val px =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    return px.toInt()
}