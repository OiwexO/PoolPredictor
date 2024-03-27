package com.iwex.poolpredictor.presentation.viewmodel.esp

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.model.EspParameters

interface PredictionViewModel {

    val espParameters: LiveData<EspParameters>

    val predictionData: LiveData<FloatArray>
}