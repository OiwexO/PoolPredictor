package com.iwex.poolpredictor.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.iwex.poolpredictor.data.NativeBridge
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.ShotResult
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.NativeRepository

class NativeRepositoryImpl : NativeRepository {

    private val _shotResult = MutableLiveData<ShotResult>()
    override val shotResult: LiveData<ShotResult> get() = _shotResult

    init {
        NativeBridge.setNativeRepository(this)
    }

    override fun updateAimSettings(aimSettings: AimSettings) = with(aimSettings) {
        NativeBridge.updateAimSettings(
            drawLinesEnabled = drawLinesEnabled,
            drawShotStateEnabled = drawShotStateEnabled,
            drawOpponentsLinesEnabled = drawOpponentsLinesEnabled,
            preciseTrajectoriesEnabled = preciseTrajectoriesEnabled,
            cuePower = cuePower,
            cueSpin = cueSpin
        )
    }


    override fun setTablePosition(tablePosition: TablePosition) = with(tablePosition) {
        NativeBridge.setTablePosition(left, top, right, bottom)
    }

    override fun updateShotResult(shotResultArray: FloatArray) {
        _shotResult.postValue(ShotResult(shotResultArray))
    }

}