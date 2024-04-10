package com.iwex.poolpredictor.presentation.viewmodel.esp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.EspParameters
import com.iwex.poolpredictor.domain.model.EspSettings
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetEspSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveEspSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.native.GetPredictionDataUseCase
import com.iwex.poolpredictor.domain.usecase.table.ResetTablePositionUseCase

class EspSharedViewModel(
    private val getEspSettingsUseCase: GetEspSettingsUseCase,
    private val saveEspSettingsUseCase: SaveEspSettingsUseCase,
    private val resetTablePositionUseCase: ResetTablePositionUseCase,
    private val getPredictionDataUseCase: GetPredictionDataUseCase
) : ViewModel(), EspTabViewModel, PredictionViewModel {

    private var espSettings = getEspSettingsUseCase()
        set(value) {
            field = value
            _espParameters.value = EspParameters(value)
        }

    private val _espParameters = MutableLiveData(EspParameters(espSettings))

    override val espParameters: LiveData<EspParameters>
        get() = _espParameters

    override val predictionData: LiveData<FloatArray>
        get() = getPredictionDataUseCase()

    override fun getEspSettings(): EspSettings = espSettings

    override fun onLineWidthChange(width: Int) {
        espSettings = espSettings.copy(lineWidth = width)
    }

    override fun onBallRadiusChange(radius: Int) {
        espSettings = espSettings.copy(ballRadius = radius)
    }

    override fun onTrajectoryOpacityChange(opacity: Int) {
        espSettings = espSettings.copy(trajectoryOpacity = opacity)
    }

    override fun onShotStateCircleWidthChange(width: Int) {
        espSettings = espSettings.copy(shotStateCircleWidth = width)
    }

    override fun onShotStateCircleRadiusChange(radius: Int) {
        espSettings = espSettings.copy(shotStateCircleRadius = radius)
    }

    override fun onShotStateCircleOpacityChange(opacity: Int) {
        espSettings = espSettings.copy(shotStateCircleOpacity = opacity)
    }

    override fun onResetTableListener() {
        resetTablePositionUseCase()
    }

    override fun saveEspSettings() {
        saveEspSettingsUseCase(espSettings)
    }

}
