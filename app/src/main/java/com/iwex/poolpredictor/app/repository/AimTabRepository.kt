package com.iwex.poolpredictor.app.repository

class AimTabRepository(private val repository: ConfigRepository) {

    companion object {
        private const val KEY_DRAW_LINES_ENABLED = "draw_lines_enabled"
        private const val KEY_DRAW_SHOT_STATE_ENABLED = "draw_shot_state_enabled"
        private const val KEY_DRAW_OPPONENTS_LINES_ENABLED = "draw_opponents_lines_enabled"
        private const val KEY_POWER_CONTROL_MODE_ENABLED = "power_control_mode_enabled"
        private const val KEY_CUE_POWER = "cue_power"
        private const val KEY_CUE_SPIN = "cue_spin"
    }

    fun getDrawLinesEnabled(): Boolean {
        return repository.getBoolean(KEY_DRAW_LINES_ENABLED, true)
    }

    fun putDrawLinesEnabled(enabled: Boolean) {
        repository.putBoolean(KEY_DRAW_LINES_ENABLED, enabled)
    }

    fun getDrawShotStateEnabled(): Boolean {
        return repository.getBoolean(KEY_DRAW_SHOT_STATE_ENABLED, true)
    }

    fun putDrawShotStateEnabled(enabled: Boolean) {
        repository.putBoolean(KEY_DRAW_SHOT_STATE_ENABLED, enabled)
    }

    fun getDrawOpponentsLinesEnabled(): Boolean {
        return repository.getBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, true)
    }

    fun putDrawOpponentsLinesEnabled(enabled: Boolean) {
        repository.putBoolean(KEY_DRAW_OPPONENTS_LINES_ENABLED, enabled)
    }

    fun getPowerControlModeEnabled(): Boolean {
        return repository.getBoolean(KEY_POWER_CONTROL_MODE_ENABLED, false)
    }

    fun putPowerControlModeEnabled(enabled: Boolean) {
        repository.putBoolean(KEY_POWER_CONTROL_MODE_ENABLED, enabled)
    }

    fun getCuePower(): Int {
        return repository.getInt(KEY_CUE_POWER, 0)
    }

    fun putCuePower(power: Int) {
        repository.putInt(KEY_CUE_POWER, power)
    }

    fun getCueSpin(): Int {
        return repository.getInt(KEY_CUE_SPIN, 0)
    }

    fun putCueSpin(spin: Int) {
        repository.putInt(KEY_CUE_SPIN, spin)
    }

}
