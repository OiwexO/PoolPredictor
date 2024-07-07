package com.iwex.poolpredictor.presentation.viewmodel.esp

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.model.EspParameters
import com.iwex.poolpredictor.domain.model.ShotResult

interface PredictionViewModel {
    val espParameters: LiveData<EspParameters>
    val shotResult: LiveData<ShotResult>
}
