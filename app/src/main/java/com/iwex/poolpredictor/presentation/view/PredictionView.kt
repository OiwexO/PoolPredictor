package com.iwex.poolpredictor.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import com.iwex.poolpredictor.domain.model.EspParameters
import com.iwex.poolpredictor.presentation.resource.EspColors
import com.iwex.poolpredictor.presentation.viewmodel.esp.PredictionViewModel

//TODO Refactor EspView
@SuppressLint("ViewConstructor")
class PredictionView(
    context: Context,
    private val viewModel: PredictionViewModel
) : NonInteractiveOverlayView(context) {

    private val trajectoryPaints = Array(NUMBER_OF_BALLS) { Paint() }
    private val shotStatePaints = Array(2) { Paint() }

    private var params = EspParameters.DEFAULT
    private var predictionData = floatArrayOf(0.0f, 0.0f)

    private val trajectoryPath = Path()

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null) //TODO check if this improves performance
        initPaints()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.espParameters.observeForever {
            params = it
            updateEspParameters()
            invalidate()
        }
        viewModel.predictionData.observeForever {
            predictionData = it
            invalidate()
        }
    }

    private fun initPaints() {
        trajectoryPath.fillType = Path.FillType.WINDING
        for (i in 0 until NUMBER_OF_BALLS) {
            trajectoryPaints[i].apply {
                color = EspColors.BALLS_COLORS[i]
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
            }

        }
        for (i in 0..1) {
            shotStatePaints[i].apply {
                color = EspColors.SHOT_STATE_COLORS[i]
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                style = Paint.Style.STROKE
            }
        }
    }

    private fun updateEspParameters() {
        for (i in 0 until NUMBER_OF_BALLS) {
            trajectoryPaints[i].apply {
                alpha = params.trajectoryOpacity
                strokeWidth = if (i < 9) params.solidLineWidth else params.stripeLineWidth

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
        /**
         * Explanation of espData:
         * 1. isTrajectoryEnabled: 0.0f or 1.0f
         *    - If (isTrajectoryEnabled):
         *      2. nOfBalls: number of balls that are currently on the table (0..16)
         *         3. for 0..nOfBalls - 1:
         *               ballIndex: 0..15, nOfBallPositions: number of ball's positions
         *                  for 0..nOfBallPositions - 1:
         *                      ballX, ballY
         * 4. isShotStateEnabled: 0.0f or 1.0f
         *    - If (isShotStateEnabled):
         *      5. for 0..5 (there are 6 pockets on the table):
         *            pocketState: 0.0f or 1.0f shows if a valid ball has been potted to this pocket, pocketX, pocketY
         */
        var index = 0
        val isTrajectoryEnabled = (predictionData[index++] == 1.0f)
        if (isTrajectoryEnabled) {
            val nOfBalls = predictionData[index++].toInt()
            var ballIndex: Int
            var nOfBallPositions: Int
            var startX: Float
            var startY: Float
            var endX: Float
            var endY: Float
            var isSolidBall: Boolean
            var ballRadius: Float
            for (i in 0 until nOfBalls) {
                ballIndex = predictionData[index++].toInt()
                nOfBallPositions = predictionData[index++].toInt()
                startX = predictionData[index++]
                startY = predictionData[index++]
                trajectoryPath.moveTo(startX, startY)
                for (j in 1 until nOfBallPositions) {
                    endX = predictionData[index++]
                    endY = predictionData[index++]
                    trajectoryPath.lineTo(endX, endY)
                    startX = endX
                    startY = endY
                }
                trajectoryPaints[ballIndex].style = Paint.Style.STROKE
                canvas.drawPath(trajectoryPath, trajectoryPaints[ballIndex])
                trajectoryPath.reset()
                isSolidBall = ballIndex < 9
                if (isSolidBall) {
                    trajectoryPaints[ballIndex].style = Paint.Style.FILL
                    ballRadius = params.solidBallRadius
                } else {
                    ballRadius = params.stripeBallRadius
                }
                canvas.drawCircle(
                    startX,
                    startY,
                    ballRadius,
                    trajectoryPaints[ballIndex]
                )
            }
        }
        val isShotStateEnabled = (predictionData[index++] == 1.0f)
        if (isShotStateEnabled) {
            var pocketState: Int
            var pocketX: Float
            var pocketY: Float
            for (i in 0 until NUMBER_OF_POCKETS) {
                pocketState = predictionData[index++].toInt()
                pocketX = predictionData[index++]
                pocketY = predictionData[index++]
                canvas.drawCircle(
                    pocketX,
                    pocketY,
                    params.shotStateCircleRadius,
                    shotStatePaints[pocketState]
                )
            }
        }
    }

    companion object {

        @Suppress("unused")
        private const val TAG = "EspView.kt"

        private const val NUMBER_OF_POCKETS = 6
        private const val NUMBER_OF_BALLS = 16
    }
}