package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.NativeBridge
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveAimSettingsUseCase

class AimTabViewModel(
    private val getAimSettingsUseCase: GetAimSettingsUseCase,
    private val saveAimSettingsUseCase: SaveAimSettingsUseCase
) : ViewModel() {

    private var aimSettings = getAimSettingsUseCase()

    init {
        with(aimSettings) {
            NativeBridge.setDrawLines(drawLinesEnabled)
            NativeBridge.setDrawShotState(drawShotStateEnabled)
            NativeBridge.setDrawOpponentsLines(drawOpponentsLinesEnabled)
            NativeBridge.setPreciseTrajectoriesEnabled(preciseTrajectoriesEnabled)
            NativeBridge.setCuePower(cuePower)
            NativeBridge.setCueSpin(cueSpin)
        }
    }

    fun getAimSettings(): AimSettings {
        return aimSettings
    }

    fun onDrawLinesChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(drawLinesEnabled = isChecked)
        NativeBridge.setDrawLines(isChecked)
    }

    fun onDrawShotStateChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(drawShotStateEnabled = isChecked)
        NativeBridge.setDrawShotState(isChecked)
    }

    fun onDrawOpponentsLinesChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(drawOpponentsLinesEnabled = isChecked)
        NativeBridge.setDrawOpponentsLines(isChecked)
    }

    fun onPreciseTrajectoriesEnabledChange(isChecked: Boolean) {
        aimSettings = aimSettings.copy(preciseTrajectoriesEnabled = isChecked)
        NativeBridge.setPreciseTrajectoriesEnabled(isChecked)
    }

    fun onCuePowerChange(power: Int) {
        aimSettings = aimSettings.copy(cuePower = power)
        NativeBridge.setCuePower(power)
    }

    fun onCueSpinChange(spin: Int) {
        aimSettings = aimSettings.copy(cueSpin = spin)
        NativeBridge.setCueSpin(spin)
    }

    fun saveState() {
        saveAimSettingsUseCase(aimSettings)
    }

}
