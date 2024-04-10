package com.iwex.poolpredictor.domain.repository

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.TablePosition

interface NativeRepository {

    fun updateAimSettings(aimSettings: AimSettings)

    fun setTablePosition(tablePosition: TablePosition)

    fun updatePredictionData(predictionData: FloatArray)

    fun getPredictionData(): LiveData<FloatArray>
}