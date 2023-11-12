package com.iwex.poolpredictor.app.repository

import com.iwex.poolpredictor.app.model.AimTabState

class AimTabRepository(private val repository: ConfigRepository) {

    companion object {
        private const val KEY_DRAW_LINES_ENABLED = "draw_lines_enabled"
        private const val KEY_DRAW_SHOT_STATE_ENABLED = "draw_shot_state_enabled"
        private const val KEY_DRAW_OPPONENTS_LINES_ENABLED = "draw_opponents_lines_enabled"
        private const val KEY_POWER_CONTROL_MODE_ENABLED = "power_control_mode_enabled"
        private const val KEY_CUE_POWER = "cue_power"
        private const val KEY_CUE_SPIN = "cue_spin"
    }

    fun getAimTabState(): AimTabState = with(repository) {
        return AimTabState(
            getBoolean(KEY_DRAW_LINES_ENABLED, false),
            getBoolean(KEY_DRAW_SHOT_STATE_ENABLED, false),
            getBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, false),
            getBoolean(KEY_POWER_CONTROL_MODE_ENABLED, false),
            getInt(KEY_CUE_POWER, 0),
            getInt(KEY_CUE_SPIN, 0)
        )
    }

    fun putAimTabState(state: AimTabState) {
        with(repository) {
            state.apply {
                putBoolean(KEY_DRAW_LINES_ENABLED, drawLinesEnabled)
                putBoolean(KEY_DRAW_SHOT_STATE_ENABLED, drawShotStateEnabled)
                putBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, drawOpponentsLinesEnabled)
                putBoolean(KEY_POWER_CONTROL_MODE_ENABLED, powerControlModeEnabled)
                putInt(KEY_CUE_POWER, cuePower)
                putInt(KEY_CUE_SPIN, cueSpin)
            }
        }
    }

}
