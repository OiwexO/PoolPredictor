package com.iwex.poolpredictor.app.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.NativeBridge
import com.iwex.poolpredictor.app.model.AimTabState
import com.iwex.poolpredictor.app.repository.AimTabRepository

class AimTabViewModel(
    private val repository: AimTabRepository
) : ViewModel() {

    private var aimTabState = repository.getAimTabState()

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
        repository.putAimTabState(aimTabState)
    }

}
