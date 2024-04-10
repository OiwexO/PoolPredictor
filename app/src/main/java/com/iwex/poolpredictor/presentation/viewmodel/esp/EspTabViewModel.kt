package com.iwex.poolpredictor.presentation.viewmodel.esp

import com.iwex.poolpredictor.domain.model.EspSettings

interface EspTabViewModel {

    fun getEspSettings(): EspSettings

    fun onLineWidthChange(width: Int)

    fun onBallRadiusChange(radius: Int)

    fun onTrajectoryOpacityChange(opacity: Int)

    fun onShotStateCircleWidthChange(width: Int)

    fun onShotStateCircleRadiusChange(radius: Int)

    fun onShotStateCircleOpacityChange(opacity: Int)

    fun onResetTableListener()

    fun saveEspSettings()
}