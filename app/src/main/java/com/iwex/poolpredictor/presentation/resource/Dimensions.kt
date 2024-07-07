package com.iwex.poolpredictor.presentation.resource

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

class Dimensions private constructor(displayMetrics: DisplayMetrics) {
    val displayHeight: Int = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
    val displayWidth: Int = max(displayMetrics.widthPixels, displayMetrics.heightPixels)

    val iconSizePx: Int = dpToPx(65f, displayMetrics).roundToInt()
    val iconPositionX: Int = displayWidth - iconSizePx
    val iconPositionY: Int = displayHeight - iconSizePx

    val menuWidthPx: Int = dpToPx(300f, displayMetrics).roundToInt()
    val menuHeightPx: Int = dpToPx(360f, displayMetrics).roundToInt()

    val tabPaddingPx: Int = dpToPx(10f, displayMetrics).roundToInt()

    val switchHeightPx: Int = dpToPx(38f, displayMetrics).roundToInt()
    val switchTextSizeSp: Float = 15f

    val seekbarHeightPx: Int = dpToPx(38f, displayMetrics).roundToInt()
    val seekbarTextSizeSp: Float = 15f

    val buttonMarginPx: Int = dpToPx(5f, displayMetrics).roundToInt()
    val buttonCornerRadiusPx: Float = dpToPx(5.5f, displayMetrics)
    val buttonTextSizeSp: Float = 15.5f

    val arrowButtonSizePx: Float = dpToPx(40f, displayMetrics)

    val titleTextSizeSp: Float = 24f

    private fun dpToPx(dp: Float, displayMetrics: DisplayMetrics): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics)
    }

    companion object {
        @Volatile
        private var instance: Dimensions? = null

        fun getInstance(context: Context): Dimensions {
            return instance ?: synchronized(this) {
                instance ?: Dimensions(context.resources.displayMetrics).also { instance = it }
            }
        }
    }
}
