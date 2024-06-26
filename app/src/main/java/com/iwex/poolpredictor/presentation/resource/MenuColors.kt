package com.iwex.poolpredictor.presentation.resource

import android.graphics.Color

object MenuColors {
    val TAB_BACKGROUND: Int = Color.parseColor("#302E2E")
    val MENU_BACKGROUND: Int = Color.parseColor("#252525")

    val MAIN_TEXT: Int = Color.parseColor("#00BCD4")

    val SEEKBAR_PROGRESS: Int = Color.parseColor("#2196F3")
    val SEEKBAR_THUMB: Int = Color.parseColor("#2196F3")

    val SWITCH_THUMB_ENABLED: Int = Color.parseColor("#2196F3")
    val SWITCH_THUMB_DISABLED: Int = Color.parseColor("#4E4B4B")
    val SWITCH_TRACK_ENABLED: Int = Color.parseColor("#2196F3")
    val SWITCH_TRACK_DISABLED: Int = Color.parseColor("#000000")

    const val BUTTON_TEXT: Int = Color.WHITE
    val BUTTON_BACKGROUND: Int = Color.parseColor("#2196F3")
}

object EspColors {
    private val WHITE = Color.rgb(255, 244, 218)
    private val YELLOW = Color.rgb(255, 174, 1)
    private val BLUE = Color.rgb(43, 103, 194)
    private val RED = Color.rgb(244, 0, 16)
    private val PURPLE = Color.rgb(82, 31, 146)
    private val ORANGE = Color.rgb(241, 95, 0)
    private val GREEN = Color.rgb(18, 144, 38)
    private val MAROON = Color.rgb(100, 18, 0)
    private val BLACK = Color.rgb(0, 0, 0)

    val TABLE_RECT_COLOR = WHITE

    val BALLS_COLORS = intArrayOf(
        WHITE,
        YELLOW,
        BLUE,
        RED,
        PURPLE,
        ORANGE,
        GREEN,
        MAROON,
        BLACK,
        YELLOW,
        BLUE,
        RED,
        PURPLE,
        ORANGE,
        GREEN,
        MAROON
    )

    val SHOT_STATE_COLORS = intArrayOf(RED, GREEN)
}