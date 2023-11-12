package com.iwex.poolpredictor.app.repository

import com.iwex.poolpredictor.app.model.EspTabState
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

    fun getEspTabState(): EspTabState = with(repository) {
        return EspTabState(
            getInt(KEY_LINE_WIDTH, DefaultEspParameters.LINE_WIDTH),
            getInt(KEY_BALL_RADIUS, DefaultEspParameters.BALL_RADIUS),
            getInt(KEY_TRAJECTORY_OPACITY, DefaultEspParameters.TRAJECTORY_OPACITY),
            getInt(KEY_SHOT_STATE_CIRCLE_WIDTH, DefaultEspParameters.SHOT_STATE_CIRCLE_WIDTH),
            getInt(KEY_SHOT_STATE_CIRCLE_RADIUS, DefaultEspParameters.SHOT_STATE_CIRCLE_RADIUS),
            getInt(KEY_SHOT_STATE_CIRCLE_OPACITY, DefaultEspParameters.SHOT_STATE_CIRCLE_OPACITY)
        )
    }

    fun putEspTabState(state: EspTabState) {
        with(repository) {
            state.apply {
                putInt(KEY_LINE_WIDTH, lineWidth)
                putInt(KEY_BALL_RADIUS, ballRadius)
                putInt(KEY_TRAJECTORY_OPACITY, trajectoryOpacity)
                putInt(KEY_SHOT_STATE_CIRCLE_WIDTH, shotStateCircleWidth)
                putInt(KEY_SHOT_STATE_CIRCLE_RADIUS, shotStateCircleRadius)
                putInt(KEY_SHOT_STATE_CIRCLE_OPACITY, shotStateCircleOpacity)
            }
        }
    }

}