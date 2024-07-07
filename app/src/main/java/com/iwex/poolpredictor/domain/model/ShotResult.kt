package com.iwex.poolpredictor.domain.model

class ShotResult(shotResultArray: FloatArray) {
    val isTrajectoryEnabled: Boolean
    val balls: List<Ball>
    val isShotStateEnabled: Boolean
    val pockets: List<Pocket>

    /**
     * Explanation of shotResultArray:
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
    init {
        var index = 0
        isTrajectoryEnabled = (shotResultArray[index++] == ENABLED)
        val ballList = mutableListOf<Ball>()
        if (isTrajectoryEnabled) {
            val nOfBalls = shotResultArray[index++].toInt()
            for (i in 0 until nOfBalls) {
                val ballIndex = shotResultArray[index++].toInt()
                val nOfBallPositions = shotResultArray[index++].toInt()
                val positions = mutableListOf<Point2D>()
                for (j in 0 until nOfBallPositions) {
                    val x = shotResultArray[index++]
                    val y = shotResultArray[index++]
                    positions.add(Point2D(x, y))
                }
                ballList.add(Ball(ballIndex, positions))
            }
        }
        balls = ballList
        isShotStateEnabled = (shotResultArray[index++] == ENABLED)
        val pocketList = mutableListOf<Pocket>()
        if (isShotStateEnabled) {
            for (i in 0 until NUMBER_OF_POCKETS) {
                val state = shotResultArray[index++].toInt()
                val x = shotResultArray[index++]
                val y = shotResultArray[index++]
                pocketList.add(Pocket(state, Point2D(x, y)))
            }
        }
        pockets = pocketList
    }

    companion object {
        private const val ENABLED = 1.0f

        val EMPTY = ShotResult(floatArrayOf(0.0f, 0.0f))
    }
}

data class Ball(
    val index: Int,
    val positions: List<Point2D>
)

data class Pocket(
    val state: Int,
    val position: Point2D
)

data class Point2D(
    val x: Float,
    val y: Float,
)
