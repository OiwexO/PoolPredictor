package com.iwex.poolpredictor.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.model.EspParameters
import com.iwex.poolpredictor.app.model.EspTabState
import com.iwex.poolpredictor.app.repository.EspTabRepository
import com.iwex.poolpredictor.app.repository.TablePositionRepository

class EspTabViewModel(
    private val repository: EspTabRepository,
    private val tablePositionRepository: TablePositionRepository
) : ViewModel() {

    private var espTabState = repository.getEspTabState()
        set(value) {
            field = value
            _espParameters.value = value.toEspParameters()
        }

    private val _espParameters = MutableLiveData(espTabState.toEspParameters())

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
        tablePositionRepository.putIsTableSet(false)
    }

    fun saveState() {
        repository.putEspTabState(espTabState)
    }

}
