package com.iwex.poolpredictor.app.model

data class AimTabState(
    val drawLinesEnabled: Boolean = false,
    val drawShotStateEnabled: Boolean = false,
    val drawOpponentsLinesEnabled: Boolean = false,
    val powerControlModeEnabled: Boolean = false,
    val cuePower: Int = 0,
    val cueSpin: Int = 0
)
