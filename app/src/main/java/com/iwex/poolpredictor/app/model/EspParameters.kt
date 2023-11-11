package com.iwex.poolpredictor.app.model

import com.iwex.poolpredictor.app.util.DefaultEspParameters

data class EspParameters(
    val solidLineWidth: Float = DefaultEspParameters.LINE_WIDTH.toFloat(),
    val stripeLineWidth: Float = DefaultEspParameters.LINE_WIDTH * DefaultEspParameters.STRIPE_LINE_WIDTH_SCALE,
    val solidBallRadius: Float = DefaultEspParameters.BALL_RADIUS.toFloat(),
    val stripeBallRadius: Float = DefaultEspParameters.BALL_RADIUS * DefaultEspParameters.STRIPE_BALL_RADIUS_SCALE,
    val trajectoryOpacity: Int = DefaultEspParameters.TRAJECTORY_OPACITY,
    val shotStateCircleWidth: Float = DefaultEspParameters.SHOT_STATE_CIRCLE_WIDTH.toFloat(),
    val shotStateCircleRadius: Float = DefaultEspParameters.SHOT_STATE_CIRCLE_RADIUS.toFloat(),
    val shotStateCircleOpacity: Int = DefaultEspParameters.SHOT_STATE_CIRCLE_OPACITY
)