package com.iwex.poolpredictor.presentation.resource

import android.graphics.Color

object MenuColors {
    const val TAB_BACKGROUND: Int = 0xFF302E2E.toInt()
    const val MENU_BACKGROUND: Int = 0xFF252525.toInt()

    const val MAIN_TEXT: Int = 0xFF00BCD4.toInt()

    const val SEEKBAR_PROGRESS: Int = 0xFF2196F3.toInt()
    const val SEEKBAR_THUMB: Int = 0xFF2196F3.toInt()

    const val SWITCH_THUMB_ENABLED: Int = 0xFF2196F3.toInt()
    const val SWITCH_THUMB_DISABLED: Int = 0xFF4E4B4B.toInt()
    const val SWITCH_TRACK_ENABLED: Int = 0xFF2196F3.toInt()
    const val SWITCH_TRACK_DISABLED: Int = 0xFF000000.toInt()

    const val BUTTON_TEXT: Int = Color.WHITE
    const val BUTTON_BACKGROUND: Int = 0xFF2196F3.toInt()
}
object EspColors {
    private const val WHITE = 0xFFFFF4DA.toInt()
    private const val YELLOW = 0xFFFFAE01.toInt()
    private const val BLUE = 0xFF2B67C2.toInt()
    private const val RED = 0xFFF40010.toInt()
    private const val PURPLE = 0xFF521F92.toInt()
    private const val ORANGE = 0xFFF15F00.toInt()
    private const val GREEN = 0xFF129026.toInt()
    private const val MAROON = 0xFF641200.toInt()
    private const val BLACK = Color.BLACK

    const val TABLE_RECT_COLOR = WHITE

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
