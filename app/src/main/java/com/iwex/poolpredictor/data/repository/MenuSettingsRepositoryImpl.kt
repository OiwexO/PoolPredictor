package com.iwex.poolpredictor.data.repository

import android.content.SharedPreferences
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.EspSettings
import com.iwex.poolpredictor.domain.repository.MenuSettingsRepository

class MenuSettingsRepositoryImpl(
    private val preferences: SharedPreferences,
) : MenuSettingsRepository {
    override fun getAimSettings() = with(preferences) {
        AimSettings(
            drawLinesEnabled = getBoolean(
                KEY_DRAW_LINES_ENABLED,
                AimSettings.DEFAULT.drawLinesEnabled
            ),
            drawShotStateEnabled = getBoolean(
                KEY_DRAW_SHOT_STATE_ENABLED,
                AimSettings.DEFAULT.drawShotStateEnabled
            ),
            drawOpponentsLinesEnabled = getBoolean(
                KEY_DRAW_OPPONENTS_LINES_ENABLED,
                AimSettings.DEFAULT.drawOpponentsLinesEnabled
            ),
            preciseTrajectoriesEnabled = getBoolean(
                KEY_PRECISE_TRAJECTORIES_ENABLED,
                AimSettings.DEFAULT.preciseTrajectoriesEnabled
            ),
            cuePower = getInt(KEY_CUE_POWER, AimSettings.DEFAULT.cuePower),
            cueSpin = getInt(KEY_CUE_SPIN, AimSettings.DEFAULT.cueSpin)
        )
    }

    override fun putAimSettings(aimSettings: AimSettings) {
        preferences.edit()
            .putBoolean(KEY_DRAW_LINES_ENABLED, aimSettings.drawLinesEnabled)
            .putBoolean(KEY_DRAW_SHOT_STATE_ENABLED, aimSettings.drawShotStateEnabled)
            .putBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, aimSettings.drawOpponentsLinesEnabled)
            .putBoolean(KEY_PRECISE_TRAJECTORIES_ENABLED, aimSettings.preciseTrajectoriesEnabled)
            .putInt(KEY_CUE_POWER, aimSettings.cuePower)
            .putInt(KEY_CUE_SPIN, aimSettings.cueSpin)
            .apply()
    }

    override fun getEspSettings() = with(preferences) {
        EspSettings(
            lineWidth = getInt(KEY_LINE_WIDTH, EspSettings.DEFAULT.lineWidth),
            ballRadius = getInt(KEY_BALL_RADIUS, EspSettings.DEFAULT.ballRadius),
            trajectoryOpacity = getInt(
                KEY_TRAJECTORY_OPACITY,
                EspSettings.DEFAULT.trajectoryOpacity
            ),
            shotStateCircleWidth = getInt(
                KEY_SHOT_STATE_CIRCLE_WIDTH,
                EspSettings.DEFAULT.shotStateCircleWidth
            ),
            shotStateCircleRadius = getInt(
                KEY_SHOT_STATE_CIRCLE_RADIUS,
                EspSettings.DEFAULT.shotStateCircleRadius
            ),
            shotStateCircleOpacity = getInt(
                KEY_SHOT_STATE_CIRCLE_OPACITY,
                EspSettings.DEFAULT.shotStateCircleOpacity
            )
        )
    }

    override fun putEspSettings(espSettings: EspSettings) {
        preferences.edit()
            .putInt(KEY_LINE_WIDTH, espSettings.lineWidth)
            .putInt(KEY_BALL_RADIUS, espSettings.ballRadius)
            .putInt(KEY_TRAJECTORY_OPACITY, espSettings.trajectoryOpacity)
            .putInt(KEY_SHOT_STATE_CIRCLE_WIDTH, espSettings.shotStateCircleWidth)
            .putInt(KEY_SHOT_STATE_CIRCLE_RADIUS, espSettings.shotStateCircleRadius)
            .putInt(KEY_SHOT_STATE_CIRCLE_OPACITY, espSettings.shotStateCircleOpacity)
            .apply()
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
