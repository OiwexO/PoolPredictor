package com.iwex.poolpredictor.data.local.repository

import android.content.SharedPreferences
import com.iwex.poolpredictor.domain.model.menu.tabs.AimTabState
import com.iwex.poolpredictor.domain.model.menu.tabs.EspTabState
import com.iwex.poolpredictor.domain.repository.MenuStateRepository

class MenuStateRepositoryImpl(private val preferences: SharedPreferences) : MenuStateRepository {

    override fun getAimTabState(): AimTabState = with(preferences) {
        return AimTabState(
            getBoolean(KEY_DRAW_LINES_ENABLED, AimTabState.DEFAULT.drawLinesEnabled),
            getBoolean(KEY_DRAW_SHOT_STATE_ENABLED, AimTabState.DEFAULT.drawShotStateEnabled),
            getBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, AimTabState.DEFAULT.drawOpponentsLinesEnabled),
            getBoolean(KEY_PRECISE_TRAJECTORIES_ENABLED, AimTabState.DEFAULT.preciseTrajectoriesEnabled),
            getInt(KEY_CUE_POWER, AimTabState.DEFAULT.cuePower),
            getInt(KEY_CUE_SPIN, AimTabState.DEFAULT.cueSpin)
        )
    }

    override fun putAimTabState(aimTabState: AimTabState) {
        with(preferences.edit()) {
            putBoolean(KEY_DRAW_LINES_ENABLED, aimTabState.drawLinesEnabled)
            putBoolean(KEY_DRAW_SHOT_STATE_ENABLED, aimTabState.drawShotStateEnabled)
            putBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, aimTabState.drawOpponentsLinesEnabled)
            putBoolean(KEY_PRECISE_TRAJECTORIES_ENABLED, aimTabState.preciseTrajectoriesEnabled)
            putInt(KEY_CUE_POWER, aimTabState.cuePower)
            putInt(KEY_CUE_SPIN, aimTabState.cueSpin)
            apply()
        }
    }

    override fun getEspTabState(): EspTabState = with(preferences) {
        return EspTabState(
            getInt(KEY_LINE_WIDTH, EspTabState.DEFAULT.lineWidth),
            getInt(KEY_BALL_RADIUS, EspTabState.DEFAULT.ballRadius),
            getInt(KEY_TRAJECTORY_OPACITY, EspTabState.DEFAULT.trajectoryOpacity),
            getInt(KEY_SHOT_STATE_CIRCLE_WIDTH, EspTabState.DEFAULT.shotStateCircleWidth),
            getInt(KEY_SHOT_STATE_CIRCLE_RADIUS, EspTabState.DEFAULT.shotStateCircleRadius),
            getInt(KEY_SHOT_STATE_CIRCLE_OPACITY, EspTabState.DEFAULT.shotStateCircleOpacity)
        )
    }

    override fun putEspTabState(espTabState: EspTabState) {
        with(preferences.edit()) {
            putInt(KEY_LINE_WIDTH, espTabState.lineWidth)
            putInt(KEY_BALL_RADIUS, espTabState.ballRadius)
            putInt(KEY_TRAJECTORY_OPACITY, espTabState.trajectoryOpacity)
            putInt(KEY_SHOT_STATE_CIRCLE_WIDTH, espTabState.shotStateCircleWidth)
            putInt(KEY_SHOT_STATE_CIRCLE_RADIUS, espTabState.shotStateCircleRadius)
            putInt(KEY_SHOT_STATE_CIRCLE_OPACITY, espTabState.shotStateCircleOpacity)
            apply()
        }
    }

    companion object {

        private const val KEY_DRAW_LINES_ENABLED = "draw_lines_enabled"
        private const val KEY_DRAW_SHOT_STATE_ENABLED = "draw_shot_state_enabled"
        private const val KEY_DRAW_OPPONENTS_LINES_ENABLED = "draw_opponents_lines_enabled"
        private const val KEY_PRECISE_TRAJECTORIES_ENABLED = "precise_trajectories_enabled"
        private const val KEY_CUE_POWER = "cue_power"
        private const val KEY_CUE_SPIN = "cue_spin"

        private const val KEY_LINE_WIDTH = "line_width"
        private const val KEY_BALL_RADIUS = "ball_radius"
        private const val KEY_TRAJECTORY_OPACITY = "trajectory_opacity"
        private const val KEY_SHOT_STATE_CIRCLE_WIDTH = "shot_state_circle_width"
        private const val KEY_SHOT_STATE_CIRCLE_RADIUS = "shot_state_circle_radius"
        private const val KEY_SHOT_STATE_CIRCLE_OPACITY = "shot_state_circle_opacity"
    }

}