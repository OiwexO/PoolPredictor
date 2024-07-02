package com.iwex.poolpredictor.domain.repository

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.ShotResult
import com.iwex.poolpredictor.domain.model.TablePosition

interface NativeRepository {

    val shotResult: LiveData<ShotResult>

    fun updateAimSettings(aimSettings: AimSettings)

    fun setTablePosition(tablePosition: TablePosition)

    fun updateShotResult(shotResultArray: FloatArray)
}