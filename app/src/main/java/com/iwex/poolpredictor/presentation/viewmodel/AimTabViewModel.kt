package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.native.UpdateAimSettingsNativeUseCase

class AimTabViewModel(
    private val getAimSettingsUseCase: GetAimSettingsUseCase,
    private val updateAimSettingsNativeUseCase: UpdateAimSettingsNativeUseCase,
    private val saveAimSettingsUseCase: SaveAimSettingsUseCase
) : ViewModel() {

    private var aimSettings: AimSettings

    init {
        aimSettings = getAimSettingsUseCase()
        updateAimSettingsNativeUseCase(aimSettings)
    }

    private fun updateAimSettings(action: AimSettings.() -> AimSettings) {
        aimSettings = aimSettings.action()
        updateAimSettingsNativeUseCase(aimSettings)
    }

    fun getAimSettings(): AimSettings = aimSettings

    fun onDrawLinesChange(isChecked: Boolean) {
        updateAimSettings { copy(drawLinesEnabled = isChecked) }
    }

    fun onDrawShotStateChange(isChecked: Boolean) {
        updateAimSettings { copy(drawShotStateEnabled = isChecked) }
    }

    fun onDrawOpponentsLinesChange(isChecked: Boolean) {
        updateAimSettings { copy(drawOpponentsLinesEnabled = isChecked) }
    }

    fun onPreciseTrajectoriesEnabledChange(isChecked: Boolean) {
        updateAimSettings { copy(preciseTrajectoriesEnabled = isChecked) }
    }

    fun onCuePowerChange(power: Int) {
        updateAimSettings { copy(cuePower = power) }
    }

    fun onCueSpinChange(spin: Int) {
        updateAimSettings { copy(cueSpin = spin) }
    }

    fun saveAimSettings() {
        saveAimSettingsUseCase(aimSettings)
    }
}
