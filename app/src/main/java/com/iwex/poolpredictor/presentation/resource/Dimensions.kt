package com.iwex.poolpredictor.presentation.resource

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class Dimensions private constructor(context: Context) {

    private val displayMetrics: DisplayMetrics = context.resources.displayMetrics

    val displayHeight: Int = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
    val displayWidth: Int = max(displayMetrics.widthPixels, displayMetrics.heightPixels)

    val iconSizePx: Int = dpToPx(65f).roundToInt()
    val iconPositionX: Int = max(displayMetrics.heightPixels, displayMetrics.widthPixels)
    val iconPositionY: Int = 0

    val menuWidthPx: Int = dpToPx(300f).roundToInt()
    val menuHeightPx: Int = dpToPx(360f).roundToInt()

    val tabPaddingPx: Int = dpToPx(10f).roundToInt()

    val switchHeightPx: Int = dpToPx(38f).roundToInt()
    val switchTextSizeSp: Float = 15f

    val seekbarHeightPx: Int = dpToPx(38f).roundToInt()
    val seekbarTextSizeSp: Float = 15f

    val buttonMarginPx: Int = dpToPx(5f).roundToInt()
    val buttonCornerRadiusPx: Float = dpToPx(5.5f)
    val buttonTextSizeSp: Float = 15.5f

    val arrowButtonSizePx: Float = dpToPx(40f)

    val titleTextSizeSp: Float = 24f

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
    }

    companion object {

        @Volatile
        private var instance: Dimensions? = null

        fun getInstance(context: Context): Dimensions {
            return instance ?: synchronized(this) {
                instance ?: Dimensions(context).also { instance = it }
            }
        }
    }
}