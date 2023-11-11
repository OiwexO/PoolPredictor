package com.iwex.poolpredictor.app.util

import android.graphics.Color




object EspColors {
    private val WHITE = Color.rgb(255, 244, 218) // ball 00
    private val YELLOW = Color.rgb(255, 174, 1)  // ball 01
    private val BLUE = Color.rgb(43, 103, 194)   // ball 02
    private val RED = Color.rgb(244, 0, 16)      // ball 03
    private val PURPLE = Color.rgb(82, 31, 146)  // ball 04
    private val ORANGE = Color.rgb(241, 95, 0)   // ball 05
    private val GREEN = Color.rgb(18, 144, 38)   // ball 06
    private val MAROON = Color.rgb(100, 18, 0)   // ball 07
    private val BLACK = Color.rgb(0, 0, 0)       // ball 08

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