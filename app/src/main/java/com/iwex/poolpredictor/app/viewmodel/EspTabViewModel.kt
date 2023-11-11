package com.iwex.poolpredictor.app.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.model.EspParameters

import com.iwex.poolpredictor.app.repository.EspTabRepository
import com.iwex.poolpredictor.app.repository.TablePositionRepository
import com.iwex.poolpredictor.app.util.DefaultEspParameters
import kotlin.math.roundToInt

class EspTabViewModel(
    private val repository: EspTabRepository,
    private val tablePositionRepository: TablePositionRepository,
    private val espViewModel: EspViewModel
) : ViewModel() {

    private var lineWidth: Int = repository.getLineWidth()
    private var ballRadius: Int = repository.getBallRadius()
    private var trajectoryOpacity: Int = repository.getTrajectoryOpacity()
    private var shotStateCircleWidth: Int = repository.getShotStateCircleWidth()
    private var shotStateCircleRadius: Int = repository.getShotStateCircleRadius()
    private var shotStateCircleOpacity: Int = repository.getShotStateCircleOpacity()

    init {
        updateEspParameters()
    }

    private fun mapOpacity(opacity: Int): Int {
        return (opacity * 2.55f).roundToInt()
    }

    private fun mapEspParameters(): EspParameters {
        return EspParameters(
            lineWidth.toFloat(),
            lineWidth * DefaultEspParameters.STRIPE_LINE_WIDTH_SCALE,
            ballRadius.toFloat(),
            ballRadius * DefaultEspParameters.STRIPE_BALL_RADIUS_SCALE,
            mapOpacity(trajectoryOpacity),
            shotStateCircleWidth.toFloat(),
            shotStateCircleRadius.toFloat(),
            mapOpacity(shotStateCircleOpacity))
    }

    private fun updateEspParameters() {
        espViewModel.setEspParameters(mapEspParameters())
    }

    fun getLineWidth(): Int {
        return lineWidth
    }

    fun getBallRadius(): Int {
        return ballRadius
    }

    fun getTrajectoryOpacity(): Int {
        return trajectoryOpacity
    }

    fun getShotStateCircleWidth(): Int {
        return shotStateCircleWidth
    }

    fun getShotStateCircleRadius(): Int {
        return shotStateCircleRadius
    }

    fun getShotStateCircleOpacity(): Int {
        return shotStateCircleOpacity
    }

    fun onLineWidthChange(width: Int) {
        lineWidth = width
        repository.putLineWidth(width)
        updateEspParameters()
    }

    fun onBallRadiusChange(radius: Int) {
        ballRadius = radius
        repository.putBallRadius(radius)
        updateEspParameters()
    }

    fun onTrajectoryOpacityChange(opacity: Int) {
        trajectoryOpacity = opacity
        repository.putTrajectoryOpacity(opacity)
        updateEspParameters()
    }

    fun onShotStateCircleWidthChange(width: Int) {
        shotStateCircleWidth = width
        repository.putShotStateCircleWidth(width)
        updateEspParameters()
    }

    fun onShotStateCircleRadiusChange(radius: Int) {
        shotStateCircleRadius = radius
        repository.putShotStateCircleRadius(radius)
        updateEspParameters()
    }

    fun onShotStateCircleOpacityChange(opacity: Int) {
        shotStateCircleOpacity = opacity
        repository.putShotStateCircleOpacity(opacity)
        updateEspParameters()
    }

    fun onResetTableListener() {
        tablePositionRepository.putIsTableSet(false)
    }

}
