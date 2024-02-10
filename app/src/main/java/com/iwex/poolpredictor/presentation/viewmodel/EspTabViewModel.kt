package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.EspParameters
import com.iwex.poolpredictor.domain.model.menu.tabs.EspTabState
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetEspTabStateUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.ResetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveEspTabStateUseCase

class EspTabViewModel(
    private val getEspTabStateUseCase: GetEspTabStateUseCase,
    private val saveEspTabStateUseCase: SaveEspTabStateUseCase,
    private val resetTablePositionUseCase: ResetTablePositionUseCase
) : ViewModel() {

    private var espTabState = getEspTabStateUseCase()
        set(value) {
            field = value
            _espParameters.value = EspParameters(value)
        }

    private val _espParameters = MutableLiveData(EspParameters(espTabState))

    val espParameters: LiveData<EspParameters>
        get() = _espParameters

    fun getEspTabState(): EspTabState {
        return espTabState
    }

    fun onLineWidthChange(width: Int) {
        espTabState = espTabState.copy(lineWidth = width)
    }

    fun onBallRadiusChange(radius: Int) {
        espTabState = espTabState.copy(ballRadius = radius)
    }

    fun onTrajectoryOpacityChange(opacity: Int) {
        espTabState = espTabState.copy(trajectoryOpacity = opacity)
    }

    fun onShotStateCircleWidthChange(width: Int) {
        espTabState = espTabState.copy(shotStateCircleWidth = width)
    }

    fun onShotStateCircleRadiusChange(radius: Int) {
        espTabState = espTabState.copy(shotStateCircleRadius = radius)
    }

    fun onShotStateCircleOpacityChange(opacity: Int) {
        espTabState = espTabState.copy(shotStateCircleOpacity = opacity)
    }

    fun onResetTableListener() {
        resetTablePositionUseCase()
    }

    fun saveState() {
        saveEspTabStateUseCase(espTabState)
    }

}
