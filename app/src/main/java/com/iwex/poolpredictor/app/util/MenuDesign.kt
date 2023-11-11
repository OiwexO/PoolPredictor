package com.iwex.poolpredictor.app.util

import android.content.Context
import android.graphics.Color
import android.util.DisplayMetrics
import android.view.ViewConfiguration
import kotlin.math.min
import kotlin.math.max
import kotlin.math.roundToInt

@Suppress("unused")
class MenuDesign private constructor() {

    companion object {
        private lateinit var displayMetrics: DisplayMetrics
        private var scaledTouchSlop: Float = 22.0f

        fun initDisplayMetrics(context: Context) {
            displayMetrics = context.resources.displayMetrics
            scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
        }

        val displayWidth: Int by lazy {
            max(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
//            get() = max(displayMetrics.widthPixels, displayMetrics.heightPixels)
        val displayHeight: Int by lazy {
            min(displayMetrics.widthPixels, displayMetrics.heightPixels)
        }
//            get() = min(displayMetrics.widthPixels, displayMetrics.heightPixels)
        val slopDistance: Float
            get() = scaledTouchSlop

        private fun dpToPx(dp: Int): Int {
            return (dp * displayMetrics.density).roundToInt()
        }

        private fun pxToDp(px: Int): Int {
            return (px / displayMetrics.density).roundToInt()
        }

    }

    object Measurements {
        val ICON_SIZE: Int = dpToPx(60)
        val MENU_WIDTH: Int = dpToPx(300)
        val MENU_HEIGHT: Int = dpToPx(360)
        const val MENU_POS_X: Int = 1000
        const val MENU_POS_Y: Int = -100
        val TAB_PADDING: Int = dpToPx(10)
        val SWITCH_HEIGHT: Int = dpToPx(38)
        val SEEKBAR_HEIGHT: Int = dpToPx(38)
        val BUTTON_MARGIN: Int = dpToPx(5)
        const val BUTTON_CORNER_RADIUS: Float = 15.0f
        const val TITLE_TEXT_SIZE: Float = 24.0f
        val ARROW_BUTTON_SIZE: Int = dpToPx(40)

    }


    object Colors {
        val TAB_BACKGROUND: Int = Color.parseColor("#302E2E")
        val MENU_BACKGROUND: Int = Color.parseColor("#252525")
        val MAIN: Int = Color.parseColor("#00BCD4")
        val SEEKBAR_PROGRESS: Int = Color.parseColor("#2196F3")
//        val SWITCH_THUMB: Int = Color.parseColor("#2196F3")
//        val SWITCH_TRACK: Int = Color.parseColor("#00BCD4")
        const val BUTTON_TEXT: Int = Color.WHITE
        val BUTTON_BACKGROUND: Int = Color.parseColor("#2196F3")

    }

}