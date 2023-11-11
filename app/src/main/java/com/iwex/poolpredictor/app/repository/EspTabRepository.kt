package com.iwex.poolpredictor.app.repository

import com.iwex.poolpredictor.app.util.DefaultEspParameters

class EspTabRepository(private val repository: ConfigRepository) {
    companion object {
        private const val KEY_LINE_WIDTH = "line_width"
        private const val KEY_BALL_RADIUS = "ball_radius"
        private const val KEY_TRAJECTORY_OPACITY = "trajectory_opacity"
        private const val KEY_SHOT_STATE_CIRCLE_WIDTH = "shot_state_circle_width"
        private const val KEY_SHOT_STATE_CIRCLE_RADIUS = "shot_state_circle_radius"
        private const val KEY_SHOT_STATE_CIRCLE_OPACITY = "shot_state_circle_opacity"

    }

    fun getLineWidth(): Int {
        return repository.getInt(KEY_LINE_WIDTH, DefaultEspParameters.LINE_WIDTH)
    }

    fun putLineWidth(width: Int) {
        repository.putInt(KEY_LINE_WIDTH, width)
    }

    fun getBallRadius(): Int {
        return repository.getInt(KEY_BALL_RADIUS, DefaultEspParameters.BALL_RADIUS)
    }

    fun putBallRadius(radius: Int) {
        repository.putInt(KEY_BALL_RADIUS, radius)
    }

    fun getTrajectoryOpacity(): Int {
        return repository.getInt(KEY_TRAJECTORY_OPACITY, DefaultEspParameters.TRAJECTORY_OPACITY)
    }

    fun putTrajectoryOpacity(opacity: Int) {
        repository.putInt(KEY_TRAJECTORY_OPACITY, opacity)
    }

    fun getShotStateCircleWidth(): Int {
        return repository.getInt(
            KEY_SHOT_STATE_CIRCLE_WIDTH,
            DefaultEspParameters.SHOT_STATE_CIRCLE_WIDTH
        )
    }

    fun putShotStateCircleWidth(width: Int) {
        repository.putInt(KEY_SHOT_STATE_CIRCLE_WIDTH, width)
    }

    fun getShotStateCircleRadius(): Int {
        return repository.getInt(
            KEY_SHOT_STATE_CIRCLE_RADIUS,
            DefaultEspParameters.SHOT_STATE_CIRCLE_RADIUS
        )
    }

    fun putShotStateCircleRadius(radius: Int) {
        repository.putInt(KEY_SHOT_STATE_CIRCLE_RADIUS, radius)
    }

    fun getShotStateCircleOpacity(): Int {
        return repository.getInt(
            KEY_SHOT_STATE_CIRCLE_OPACITY,
            DefaultEspParameters.SHOT_STATE_CIRCLE_OPACITY
        )
    }

    fun putShotStateCircleOpacity(opacity: Int) {
        repository.putInt(KEY_SHOT_STATE_CIRCLE_OPACITY, opacity)
    }

}