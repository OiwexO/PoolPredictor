package com.iwex.poolpredictor.domain.repository

import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.TablePosition

interface NativeRepository {

    fun updateAimSettings(aimSettings: AimSettings)

    fun setTablePosition(tablePosition: TablePosition)

}