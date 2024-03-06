package com.iwex.poolpredictor.domain.model

data class AimSettings(
    val drawLinesEnabled: Boolean,
    val drawShotStateEnabled: Boolean,
    val drawOpponentsLinesEnabled: Boolean,
    val preciseTrajectoriesEnabled: Boolean,
    val cuePower: Int,
    val cueSpin: Int
) {

    companion object {

        val DEFAULT = AimSettings(
            drawLinesEnabled = false,
            drawShotStateEnabled = false,
            drawOpponentsLinesEnabled = false,
            preciseTrajectoriesEnabled = false,
            cuePower = 0,
            cueSpin = 0
        )
    }

}
