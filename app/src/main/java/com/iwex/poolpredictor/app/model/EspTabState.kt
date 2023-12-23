package com.iwex.poolpredictor.app.model

import com.iwex.poolpredictor.app.util.DefaultEspParameters
import kotlin.math.roundToInt

data class EspTabState(
    val lineWidth: Int = DefaultEspParameters.LINE_WIDTH,
    val ballRadius: Int = DefaultEspParameters.BALL_RADIUS,
    val trajectoryOpacity: Int = DefaultEspParameters.TRAJECTORY_OPACITY,
    val shotStateCircleWidth: Int = DefaultEspParameters.SHOT_STATE_CIRCLE_WIDTH,
    val shotStateCircleRadius: Int = DefaultEspParameters.SHOT_STATE_CIRCLE_RADIUS,
    val shotStateCircleOpacity: Int = DefaultEspParameters.SHOT_STATE_CIRCLE_OPACITY
) {
    fun toEspParameters(): EspParameters {
        val stripeLineWidth = lineWidth * DefaultEspParameters.STRIPE_LINE_WIDTH_SCALE
        return EspParameters(
            lineWidth.toFloat(),
            stripeLineWidth,
            ballRadius.toFloat(),
            ballRadius - stripeLineWidth / 2f,
            (trajectoryOpacity * 2.55f).roundToInt(),
            shotStateCircleWidth.toFloat(),
            shotStateCircleRadius.toFloat(),
            (shotStateCircleOpacity * 2.55f).roundToInt()
        )
    }
}
