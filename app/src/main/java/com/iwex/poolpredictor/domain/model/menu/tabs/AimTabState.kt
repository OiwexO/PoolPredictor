package com.iwex.poolpredictor.domain.model.menu.tabs

data class AimTabState(
    val drawLinesEnabled: Boolean,
    val drawShotStateEnabled: Boolean,
    val drawOpponentsLinesEnabled: Boolean,
    val preciseTrajectoriesEnabled: Boolean,
    val cuePower: Int,
    val cueSpin: Int
) {

    companion object {

        val DEFAULT = AimTabState(
            drawLinesEnabled = false,
            drawShotStateEnabled = false,
            drawOpponentsLinesEnabled = false,
            preciseTrajectoriesEnabled = false,
            cuePower = 0,
            cueSpin = 0
        )
    }

}
