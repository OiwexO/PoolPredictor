package com.iwex.poolpredictor.app.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.NativeBridge
import com.iwex.poolpredictor.app.repository.AimTabRepository

class AimTabViewModel(
    private val aimTabRepository: AimTabRepository
) : ViewModel() {
    private var drawLinesEnabled = aimTabRepository.getDrawLinesEnabled()
    private var drawShotStateEnabled = aimTabRepository.getDrawShotStateEnabled()
    private var drawOpponentsLinesEnabled = aimTabRepository.getDrawOpponentsLinesEnabled()
    private var powerControlModeEnabled = aimTabRepository.getPowerControlModeEnabled()
    private var cuePower = aimTabRepository.getCuePower()
    private var cueSpin = aimTabRepository.getCueSpin()

    fun getDrawLinesEnabled(): Boolean {
        return drawLinesEnabled
    }

    fun getDrawShotStateEnabled(): Boolean {
        return drawShotStateEnabled
    }

    fun getDrawOpponentsLinesEnabled(): Boolean {
        return drawOpponentsLinesEnabled
    }

    fun getPowerControlModeEnabled(): Boolean {
        return powerControlModeEnabled
    }

    fun getCuePower(): Int {
        return cuePower
    }

    fun getCueSpin(): Int {
        return cueSpin
    }

    fun onDrawLinesChange(isChecked: Boolean) {
        drawLinesEnabled = isChecked
        aimTabRepository.putDrawLinesEnabled(isChecked)
        NativeBridge.setDrawLines(isChecked)
    }

    fun onDrawShotStateChange(isChecked: Boolean) {
        drawShotStateEnabled = isChecked
        aimTabRepository.putDrawShotStateEnabled(isChecked)
        NativeBridge.setDrawShotState(isChecked)
    }

    fun onDrawOpponentsLinesChange(isChecked: Boolean) {
        drawOpponentsLinesEnabled = isChecked
        aimTabRepository.putDrawOpponentsLinesEnabled(isChecked)
        NativeBridge.setDrawOpponentsLines(isChecked)
    }

    fun onPowerControlModeEnabledChange(isChecked: Boolean) {
        powerControlModeEnabled = isChecked
        aimTabRepository.putPowerControlModeEnabled(isChecked)
        NativeBridge.setPowerControlModeEnabled(isChecked)
    }

    fun onCuePowerChange(power: Int) {
        cuePower = power
        aimTabRepository.putCuePower(power)
        NativeBridge.setCuePower(power)
    }

    fun onCueSpinChange(spin: Int) {
        cueSpin = spin
        aimTabRepository.putCueSpin(spin)
        NativeBridge.setCueSpin(spin)
    }

}
