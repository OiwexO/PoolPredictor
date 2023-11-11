package com.iwex.poolpredictor.app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.model.EspParameters

class EspViewModel : ViewModel() {

    private var espParameters = MutableLiveData(EspParameters())

    companion object {
        const val NUMBER_OF_POCKETS = 6
    }

    fun getEspParameters(): LiveData<EspParameters> {
        return espParameters
    }

    fun setEspParameters(espParameters: EspParameters) {
        this.espParameters.value = espParameters
    }

    fun getSolidLineWidth(): Float {
        return espParameters.value!!.solidLineWidth
    }

    fun getStripeLineWidth(): Float {
        return espParameters.value!!.stripeLineWidth
    }

    fun getSolidBallRadius(): Float {
        return espParameters.value!!.solidBallRadius
    }

    fun getStripeBallRadius(): Float {
        return espParameters.value!!.stripeBallRadius
    }

    fun getTrajectoryOpacity(): Int {
        return espParameters.value!!.trajectoryOpacity
    }

    fun getShotStateCircleWidth(): Float {
        return espParameters.value!!.shotStateCircleWidth
    }

    fun getShotStateCircleRadius(): Float {
        return espParameters.value!!.shotStateCircleRadius
    }

    fun getShotStateCircleOpacity(): Int {
        return espParameters.value!!.shotStateCircleOpacity
    }

}