package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import com.iwex.poolpredictor.app.NativeBridge
import com.iwex.poolpredictor.app.model.EspParameters
import com.iwex.poolpredictor.app.model.TablePosition
import com.iwex.poolpredictor.app.util.EspColors
import com.iwex.poolpredictor.app.viewmodel.EspTabViewModel

@SuppressLint("ViewConstructor")
class EspView(context: Context, private val viewModel: EspTabViewModel) : View(context) {

    private val ballsPaints = Array(16) { Paint() }
    private val shotStatePaints = Array(2) { Paint() }
    private var espParameters = EspParameters()
    private var espData = floatArrayOf(0.0f, 0.0f)
    private var pocketPositions = FloatArray(12)
    private var isCanUpdateEspData = false

    companion object {
        @Suppress("unused")
        private const val TAG = "EspView.kt"
        private const val NUMBER_OF_POCKETS = 6
    }

    init {
        viewModel.espParameters.observeForever {
            espParameters = it
            updateEspParameters()
            postInvalidate()
        }

        initPaints()
    }

    private fun initPaints() {
        for (i in ballsPaints.indices) {
            ballsPaints[i].apply {
                color = EspColors.BALLS_COLORS[i]
                isAntiAlias = true
                strokeCap = Paint.Cap.ROUND
                style = if (i < 9) {
                    Paint.Style.FILL
                } else {
                    Paint.Style.STROKE
                }

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
        for (i in ballsPaints.indices) {
            ballsPaints[i].apply {
                alpha = espParameters.trajectoryOpacity
                strokeWidth = if (i < 9) {
                    espParameters.solidLineWidth
                } else {
                    espParameters.stripeLineWidth
                }

            }
        }

        for (i in shotStatePaints.indices) {
            shotStatePaints[i].apply {
                alpha = espParameters.shotStateCircleOpacity
                strokeWidth = espParameters.shotStateCircleWidth
            }
        }

    }

    // called from cpp/bridge/NativeBridge/predictor_thread only
    @Suppress("unused")
    fun updateEspData(data: FloatArray) {
        if (isCanUpdateEspData) {
            espData = data
            postInvalidate()
        }
    }

    // should be called before updateEspData
    fun getPocketPositionsInScreen(tablePosition: TablePosition) {
        pocketPositions = NativeBridge.getPocketPositionsInScreen(
            tablePosition.left, tablePosition.top, tablePosition.right, tablePosition.bottom
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        isCanUpdateEspData = false
        if (espData.isEmpty()) {
            isCanUpdateEspData = true
            return
        }

//        val espData = this.espData.clone()
        /*
         espData explanation:
         1. isTrajectoryEnabled: 0.0f or 1.0f
         if (isTrajectoryEnabled):
             2. nOfBalls: number of balls that are currently on the table
              3. for 0..nOfBalls - 1:
                    ballIndex: 0..15, nOfBallPositions: number of ball's collisions positions
                        for 0..nOfBallPositions - 1:
                            ballX, ballY
         4. isShotStateEnabled: 0.0f or 1.0f
         if (isShotStateEnabled):
             5. for 0..5 (there are 6 pockets on the table):
                   pocketState: 0.0f or 1.0f shows if valid ball has been potted to this pocket, pocketX, pocketY
        */

        // draw trajectories
        var index = 0
        val isTrajectoryEnabled = (espData[index++] == 1.0f)
        if (isTrajectoryEnabled) {
            val nOfBalls = espData[index++].toInt()
            var ballIndex: Int
            var nOfBallPositions: Int
            var startX: Float
            var startY: Float
            var endX: Float
            var endY: Float

            for (i in 0 until nOfBalls) {
                ballIndex = espData[index++].toInt()
                nOfBallPositions = espData[index++].toInt()
                startX = espData[index++]
                startY = espData[index++]

                for (j in 1 until nOfBallPositions) {
                    endX = espData[index++]
                    endY = espData[index++]
                    canvas.drawLine(startX, startY, endX, endY, ballsPaints[ballIndex])
                    startX = endX
                    startY = endY
                }

                canvas.drawCircle(startX, startY, if (ballIndex < 9) espParameters.solidBallRadius else espParameters.stripeBallRadius, ballsPaints[ballIndex])
            }
        }

        // draw shot state
        val isShotStateEnabled = (espData[index++] == 1.0f)
        if (isShotStateEnabled) {
            var pocketState: Int
            for (i in 0 until NUMBER_OF_POCKETS) {
                pocketState = espData[index++].toInt()
                canvas.drawCircle(
                    pocketPositions[i],
                    pocketPositions[i + NUMBER_OF_POCKETS],
                    espParameters.shotStateCircleRadius,
                    shotStatePaints[pocketState]
                )
            }
        }

        isCanUpdateEspData = true

    }
}