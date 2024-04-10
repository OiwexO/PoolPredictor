package com.iwex.poolpredictor.data.local.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iwex.poolpredictor.data.NativeBridge
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.NativeRepository

class NativeRepositoryImpl : NativeRepository {

    private val predictionData = MutableLiveData<FloatArray>()

    init {
        NativeBridge.setNativeRepository(this)
    }

    override fun updateAimSettings(aimSettings: AimSettings) {
        with(aimSettings) {
            NativeBridge.updateAimSettings(
                drawLinesEnabled = drawLinesEnabled,
                drawShotStateEnabled = drawShotStateEnabled,
                drawOpponentsLinesEnabled = drawOpponentsLinesEnabled,
                preciseTrajectoriesEnabled = preciseTrajectoriesEnabled,
                cuePower = cuePower,
                cueSpin = cueSpin
            )
        }
    }

    override fun setTablePosition(tablePosition: TablePosition) {
        NativeBridge.setTablePosition(
            left = tablePosition.left,
            top = tablePosition.top,
            right = tablePosition.right,
            bottom = tablePosition.bottom
        )
    }

    override fun updatePredictionData(predictionData: FloatArray) {
        this.predictionData.postValue(predictionData)
    }

    override fun getPredictionData(): LiveData<FloatArray> {
        return predictionData
    }
}