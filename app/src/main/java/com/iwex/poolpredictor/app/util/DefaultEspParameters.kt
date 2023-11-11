package com.iwex.poolpredictor.app.util

object DefaultEspParameters {
    // LINE parameters
    const val LINE_WIDTH = 4
    const val STRIPE_LINE_WIDTH_SCALE = 0.5f

    // BALL parameters
    const val BALL_RADIUS = 20
    const val STRIPE_BALL_RADIUS_SCALE = 0.85f

    // Trajectory parameters
    const val TRAJECTORY_OPACITY = 100

    // Shot state circle parameters
    const val SHOT_STATE_CIRCLE_WIDTH = 8
    const val SHOT_STATE_CIRCLE_RADIUS = BALL_RADIUS * 2
    const val SHOT_STATE_CIRCLE_OPACITY = 100
}