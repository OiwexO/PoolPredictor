package com.iwex.poolpredictor.data.local.repository

import com.iwex.poolpredictor.data.NativeBridge
import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.NativeRepository

class NativeRepositoryImpl : NativeRepository {
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
        NativeBridge.getPocketPositionsInScreen(
            left = tablePosition.left,
            top = tablePosition.top,
            right = tablePosition.right,
            bottom = tablePosition.bottom
        )
    }
}