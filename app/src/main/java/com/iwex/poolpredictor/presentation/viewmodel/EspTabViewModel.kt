package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.EspParameters
import com.iwex.poolpredictor.domain.model.EspSettings
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetEspSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.ResetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveEspSettingsUseCase

class EspTabViewModel(
    private val getEspSettingsUseCase: GetEspSettingsUseCase,
    private val saveEspSettingsUseCase: SaveEspSettingsUseCase,
    private val resetTablePositionUseCase: ResetTablePositionUseCase
) : ViewModel() {

    private var espSettings = getEspSettingsUseCase()
        set(value) {
            field = value
            _espParameters.value = EspParameters(value)
        }

    private val _espParameters = MutableLiveData(EspParameters(espSettings))

    val espParameters: LiveData<EspParameters>
        get() = _espParameters

    fun getEspSettings(): EspSettings {
        return espSettings
    }

    fun onLineWidthChange(width: Int) {
        espSettings = espSettings.copy(lineWidth = width)
    }

    fun onBallRadiusChange(radius: Int) {
        espSettings = espSettings.copy(ballRadius = radius)
    }

    fun onTrajectoryOpacityChange(opacity: Int) {
        espSettings = espSettings.copy(trajectoryOpacity = opacity)
    }

    fun onShotStateCircleWidthChange(width: Int) {
        espSettings = espSettings.copy(shotStateCircleWidth = width)
    }

    fun onShotStateCircleRadiusChange(radius: Int) {
        espSettings = espSettings.copy(shotStateCircleRadius = radius)
    }

    fun onShotStateCircleOpacityChange(opacity: Int) {
        espSettings = espSettings.copy(shotStateCircleOpacity = opacity)
    }

    fun onResetTableListener() {
        resetTablePositionUseCase()
    }

    fun saveState() {
        saveEspSettingsUseCase(espSettings)
    }

}
