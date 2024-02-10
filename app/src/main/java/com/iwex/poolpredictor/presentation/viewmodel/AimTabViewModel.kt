package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.NativeBridge
import com.iwex.poolpredictor.domain.model.menu.tabs.AimTabState
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetAimTabStateUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveAimTabStateUseCase

class AimTabViewModel(
    private val getAimTabStateUseCase: GetAimTabStateUseCase,
    private val saveAimTabStateUseCase: SaveAimTabStateUseCase
) : ViewModel() {

    private var aimTabState = getAimTabStateUseCase()

    init {
        with(aimTabState) {
            NativeBridge.setDrawLines(drawLinesEnabled)
            NativeBridge.setDrawShotState(drawShotStateEnabled)
            NativeBridge.setDrawOpponentsLines(drawOpponentsLinesEnabled)
            NativeBridge.setPreciseTrajectoriesEnabled(preciseTrajectoriesEnabled)
            NativeBridge.setCuePower(cuePower)
            NativeBridge.setCueSpin(cueSpin)
        }
    }

    fun getAimTabState(): AimTabState {
        return aimTabState
    }

    fun onDrawLinesChange(isChecked: Boolean) {
        aimTabState = aimTabState.copy(drawLinesEnabled = isChecked)
        NativeBridge.setDrawLines(isChecked)
    }

    fun onDrawShotStateChange(isChecked: Boolean) {
        aimTabState = aimTabState.copy(drawShotStateEnabled = isChecked)
        NativeBridge.setDrawShotState(isChecked)
    }

    fun onDrawOpponentsLinesChange(isChecked: Boolean) {
        aimTabState = aimTabState.copy(drawOpponentsLinesEnabled = isChecked)
        NativeBridge.setDrawOpponentsLines(isChecked)
    }

    fun onPreciseTrajectoriesEnabledChange(isChecked: Boolean) {
        aimTabState = aimTabState.copy(preciseTrajectoriesEnabled = isChecked)
        NativeBridge.setPreciseTrajectoriesEnabled(isChecked)
    }

    fun onCuePowerChange(power: Int) {
        aimTabState = aimTabState.copy(cuePower = power)
        NativeBridge.setCuePower(power)
    }

    fun onCueSpinChange(spin: Int) {
        aimTabState = aimTabState.copy(cueSpin = spin)
        NativeBridge.setCueSpin(spin)
    }

    fun saveState() {
        saveAimTabStateUseCase(aimTabState)
    }

}
