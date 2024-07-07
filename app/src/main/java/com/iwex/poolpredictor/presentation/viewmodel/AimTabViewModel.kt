package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.native.UpdateAimSettingsNativeUseCase

class AimTabViewModel(
    getAimSettingsUseCase: GetAimSettingsUseCase,
    private val updateAimSettingsNativeUseCase: UpdateAimSettingsNativeUseCase,
    private val saveAimSettingsUseCase: SaveAimSettingsUseCase
) : ViewModel() {
    private var aimSettings = getAimSettingsUseCase()
        set(value) {
            field = value
            updateAimSettingsNativeUseCase(aimSettings)
        }

    init {
        updateAimSettingsNativeUseCase(aimSettings)
    }

    fun getAimSettings(): AimSettings = aimSettings

    fun onDrawLinesChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(drawLinesEnabled = isChecked)
    }

    fun onDrawShotStateChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(drawShotStateEnabled = isChecked)
    }

    fun onDrawOpponentsLinesChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(drawOpponentsLinesEnabled = isChecked)
    }

    fun onPreciseTrajectoriesEnabledChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(preciseTrajectoriesEnabled = isChecked)
    }

    fun onCuePowerChange(power: Int) {
        aimSettings = aimSettings.copy(cuePower = power)
    }

    fun onCueSpinChange(spin: Int) {
        aimSettings = aimSettings.copy(cueSpin = spin)
    }

    fun saveAimSettings() {
        saveAimSettingsUseCase(aimSettings)
    }
}
