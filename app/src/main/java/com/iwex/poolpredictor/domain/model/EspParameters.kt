package com.iwex.poolpredictor.domain.model

import com.iwex.poolpredictor.domain.model.menu.tabs.EspTabState

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

    constructor(espTabState: EspTabState) : this(
        solidLineWidth = espTabState.lineWidth.toFloat(),
        stripeLineWidth = espTabState.lineWidth * STRIPE_LINE_WIDTH_SCALE,
        solidBallRadius = espTabState.ballRadius.toFloat(),
        stripeBallRadius = espTabState.ballRadius - espTabState.lineWidth * STRIPE_BALL_RADIUS_SCALE,
        trajectoryOpacity = espTabState.trajectoryOpacity,
        shotStateCircleWidth = espTabState.shotStateCircleWidth.toFloat(),
        shotStateCircleRadius = espTabState.shotStateCircleRadius.toFloat(),
        shotStateCircleOpacity = espTabState.shotStateCircleOpacity
    )

    companion object {

        private const val STRIPE_LINE_WIDTH_SCALE = 0.5f
        private const val STRIPE_BALL_RADIUS_SCALE = 0.25f

        val DEFAULT = EspParameters(EspTabState.DEFAULT)
    }
}