package com.iwex.poolpredictor.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.iwex.poolpredictor.domain.model.EspParameters
import com.iwex.poolpredictor.domain.model.NUMBER_OF_BALLS
import com.iwex.poolpredictor.domain.model.Point2D
import com.iwex.poolpredictor.domain.model.ShotResult
import com.iwex.poolpredictor.domain.model.isSolidBall
import com.iwex.poolpredictor.presentation.resource.EspColors
import com.iwex.poolpredictor.presentation.viewmodel.esp.PredictionViewModel

@SuppressLint("ViewConstructor")
class PredictionView(
    context: Context,
    private val viewModel: PredictionViewModel
) : NonInteractiveOverlayView(context) {

    private val trajectoryPaints = Array(NUMBER_OF_BALLS) { Paint() }
    private val shotStatePaints = Array(2) { Paint() }
    private val trajectoryPath = Path()

    private var params = EspParameters.DEFAULT
    private var shotResult = ShotResult.EMPTY

    init {
        initPaints()
        observeViewModel()
    }

    private fun initPaints() {
        for (i in trajectoryPaints.indices) {
            trajectoryPaints[i].apply {
                color = EspColors.BALLS_COLORS[i]
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                strokeJoin = Paint.Join.ROUND
            }
        }
        for (i in shotStatePaints.indices) {
            shotStatePaints[i].apply {
                color = EspColors.SHOT_STATE_COLORS[i]
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                style = Paint.Style.STROKE
            }
        }
    }

    private fun observeViewModel() {
        viewModel.espParameters.observeForever {
            params = it
            updateEspParameters()
            invalidate()
        }
        viewModel.shotResult.observeForever {
            shotResult = it
            invalidate()
        }
    }

    private fun updateEspParameters() {
        for (i in trajectoryPaints.indices) {
            trajectoryPaints[i].apply {
                alpha = params.trajectoryOpacity
                strokeWidth = params.getLineWidth(i)
            }
        }
        for (i in shotStatePaints.indices) {
            shotStatePaints[i].apply {
                alpha = params.shotStateCircleOpacity
                strokeWidth = params.shotStateCircleWidth
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (shotResult.isTrajectoryEnabled) {
            for (ball in shotResult.balls) {
                prepareTrajectoryPath(ball.positions)
                val index = ball.index
                val paint = trajectoryPaints[index]
                paint.style = Paint.Style.STROKE
                canvas.drawPath(trajectoryPath, paint)
                val ballRadius = params.getBallRadius(index)
                paint.style = if (isSolidBall(index)) Paint.Style.FILL else Paint.Style.STROKE
                val end = ball.positions.last()
                canvas.drawCircle(end.x, end.y, ballRadius, paint)
            }
        }
        if (shotResult.isShotStateEnabled) {
            for (pocket in shotResult.pockets) {
                canvas.drawCircle(
                    pocket.position.x,
                    pocket.position.y,
                    params.shotStateCircleRadius,
                    shotStatePaints[pocket.state]
                )
            }
        }
    }

    private fun prepareTrajectoryPath(positions: List<Point2D>) {
        val start = positions[0]
        trajectoryPath.reset()
        trajectoryPath.moveTo(start.x, start.y)
        for (i in 1 until positions.size) {
            val next = positions[i]
            trajectoryPath.lineTo(next.x, next.y)
        }
    }
}