package com.iwex.poolpredictor.domain.model

import kotlin.math.roundToInt

data class EspParameters(
    val solidLineWidth: Float,
    val stripeLineWidth: Float,
    val solidBallRadius: Float,
    val stripeBallRadius: Float,
    val trajectoryOpacity: Int,
    val shotStateCircleWidth: Float,
    val shotStateCircleRadius: Float,
    val shotStateCircleOpacity: Int
) {

    constructor(espSettings: EspSettings) : this(
        solidLineWidth = espSettings.lineWidth.toFloat(),
        stripeLineWidth = espSettings.lineWidth * STRIPE_LINE_WIDTH_SCALE,
        solidBallRadius = espSettings.ballRadius.toFloat(),
        stripeBallRadius = espSettings.ballRadius - espSettings.lineWidth * STRIPE_BALL_RADIUS_SCALE,
        trajectoryOpacity = (espSettings.trajectoryOpacity * 2.55f).roundToInt(),
        shotStateCircleWidth = espSettings.shotStateCircleWidth.toFloat(),
        shotStateCircleRadius = espSettings.shotStateCircleRadius.toFloat(),
        shotStateCircleOpacity = (espSettings.shotStateCircleOpacity * 2.55f).roundToInt()
    )

    companion object {

        private const val STRIPE_LINE_WIDTH_SCALE = 0.5f
        private const val STRIPE_BALL_RADIUS_SCALE = 0.25f

        val DEFAULT = EspParameters(EspSettings.DEFAULT)
    }
}