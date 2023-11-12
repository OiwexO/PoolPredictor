package com.iwex.poolpredictor.app.viewmodel

import android.graphics.Path
import android.graphics.PointF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.model.TablePosition
import com.iwex.poolpredictor.app.repository.TablePositionRepository
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.interfaces.OnButtonClickListener
import kotlin.math.roundToInt

class TablePositionViewModel(private val repository: TablePositionRepository): ViewModel() {

    var onButtonClickListener: OnButtonClickListener? = null

    private val defaultPosition: TablePosition

    private val _position = MutableLiveData<TablePosition>()

    val position: LiveData<TablePosition> get() = _position

    private var isTopLeftPointSet = false

    private var left: Int

    private var top: Int

    private var right: Int

    private var bottom: Int

    private var scale = 0f

    private val tableShape = arrayOf(
        PointF(-127f, 53.5f),
        PointF(-136.9f, 64.1f),
        PointF(-138.2f, 69.2f),
        PointF(-136.7f, 73.2f),
        PointF(-132.7f, 74.7f),
        PointF(-127.6f, 73.4f),
        PointF(-117f, 63.5f),
        PointF(-7.8f, 63.5f),
        PointF(-6.1f, 68.6f),
        PointF(-5.7f, 72.7f),
        PointF(-3.7f, 75.4f),
        PointF(0f, 76.7f),
        PointF(3.7f, 75.4f),
        PointF(5.7f, 72.7f),
        PointF(6.1f, 68.6f),
        PointF(7.8f, 63.5f),
        PointF(117f, 63.5f),
        PointF(127.6f, 73.4f),
        PointF(132.7f, 74.7f),
        PointF(136.7f, 73.2f),
        PointF(138.2f, 69.2f),
        PointF(136.9f, 64.1f),
        PointF(127f, 53.5f),
        PointF(127f, -53.5f),
        PointF(136.9f, -64.1f),
        PointF(138.2f, -69.2f),
        PointF(136.7f, -73.2f),
        PointF(132.7f, -74.7f),
        PointF(127.6f, -73.4f),
        PointF(117f, -63.5f),
        PointF(7.8f, -63.5f),
        PointF(6.1f, -68.6f),
        PointF(5.7f, -72.7f),
        PointF(3.7f, -75.4f),
        PointF(0f, -76.7f),
        PointF(-3.7f, -75.4f),
        PointF(-5.7f, -72.7f),
        PointF(-6.1f, -68.6f),
        PointF(-7.8f, -63.5f),
        PointF(-117f, -63.5f),
        PointF(-127.6f, -73.4f),
        PointF(-132.7f, -74.7f),
        PointF(-136.7f, -73.2f),
        PointF(-138.2f, -69.2f),
        PointF(-136.9f, -64.1f),
        PointF(-127f, -53.5f)
    )

    val isTableSet: Boolean
        get() {
            return repository.getIsTableSet()
        }

    val tablePosition: TablePosition
        get() {
            return repository.getTablePosition()
        }

    val pointNumber: Int
        get() {
            return if (isTopLeftPointSet) 2 else 1
        }

    val tableShapePath: Path
        get() {
            val path = Path()
            val start = toScreen(tableShape[0])
            var nextPoint: PointF
            path.moveTo(start.x, start.y)
            for (i in 1..tableShape.lastIndex) {
                nextPoint = toScreen(tableShape[i])
                path.lineTo(nextPoint.x, nextPoint.y)
            }
            path.close()
            return path
        }


    init {
        val displayWidth = MenuDesign.displayWidth
        val displayHeight = MenuDesign.displayHeight

        val defaultLeft = (displayWidth * 0.1697416974169742).roundToInt()    // width * myTableLeft / myScreenWidth
        val defaultTop = (displayHeight * 0.2370370370370370).roundToInt()    // height * myTableTop / myScreenHeight
        val defaultRight = (displayWidth * 0.8427121771217713).roundToInt()   // width * myTableRight / myScreenWidth
        val defaultBottom = (displayHeight * 0.9120370370370371).roundToInt() // height * myTableBottom / myScreenHeight
        defaultPosition = TablePosition(defaultLeft, defaultTop, defaultRight, defaultBottom)

        left = defaultLeft
        top = defaultTop
        right = defaultRight
        bottom = defaultBottom

        updatePositionAndScale()
    }

    fun decreaseX() {
        if (isTopLeftPointSet) right-- else left--
        updatePositionAndScale()
    }

    fun increaseX() {
        if (isTopLeftPointSet) right++ else left++
        updatePositionAndScale()
    }

    fun decreaseY() {
        if (isTopLeftPointSet) bottom-- else top--
        updatePositionAndScale()
    }

    fun increaseY() {
        if (isTopLeftPointSet) bottom++ else top++
        updatePositionAndScale()
    }

    fun resetPosition() {
        isTopLeftPointSet = false
        left = defaultPosition.left
        top = defaultPosition.top
        right = defaultPosition.right
        bottom = defaultPosition.bottom
        updatePositionAndScale()
    }

    fun savePosition() {
        if (right > left && bottom > top) {
            if (isTopLeftPointSet) {
                val currentPosition = _position.value ?: return
                repository.putIsTableSet(true)
                repository.putTablePosition(currentPosition)
                onButtonClickListener?.onButtonClicked()
            } else {
                isTopLeftPointSet = true
            }
        }

    }

    private fun updatePositionAndScale() {
        scale = (right - left) / TABLE_WIDTH
        _position.value = TablePosition(left, top, right, bottom)
    }

    private fun toScreen(point: PointF) : PointF {
        val srcX = (left + (point.x + TABLE_HALF_WIDTH) * scale)
        val srcY = (top + (point.y + TABLE_HALF_HEIGHT) * scale)
        return PointF(srcX, srcY)
    }

    companion object {
        private const val TABLE_WIDTH = 254f
        private const val TABLE_HALF_WIDTH = TABLE_WIDTH / 2f
        private const val TABLE_HEIGHT = 127f
        private const val TABLE_HALF_HEIGHT = TABLE_HEIGHT / 2f
    }

}