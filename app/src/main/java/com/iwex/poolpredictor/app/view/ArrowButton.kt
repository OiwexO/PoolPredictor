package com.iwex.poolpredictor.app.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import com.iwex.poolpredictor.app.util.MenuDesign

class ArrowButton @JvmOverloads constructor(
    context: Context,
    size: Int = MenuDesign.Measurements.ARROW_BUTTON_SIZE,
    arrowDirection: ArrowDirection = ArrowDirection.LEFT
) : View(context) {
    private val buttonSize: Float = size.coerceAtLeast(0).toFloat()
    private val arrowSize: Float = buttonSize * 0.5f

    private val paint = Paint()
    private val arrowPath = Path()

    init {
        setLayerType(LAYER_TYPE_HARDWARE, null)

        paint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
        }

        id = generateViewId()
        initPath(arrowDirection)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidthMeasureSpec = MeasureSpec.makeMeasureSpec(buttonSize.toInt(), MeasureSpec.EXACTLY)
        val desiredHeightMeasureSpec = MeasureSpec.makeMeasureSpec(buttonSize.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(desiredWidthMeasureSpec, desiredHeightMeasureSpec)
    }

    private fun initPath(arrowDirection: ArrowDirection) {
        val center = buttonSize / 2f
        val halfArrowSize = arrowSize / 2f

        arrowPath.reset()
        when (arrowDirection) {
            ArrowDirection.LEFT -> {
                arrowPath.moveTo(center - halfArrowSize, center)
                arrowPath.lineTo(center + halfArrowSize, center - halfArrowSize)
                arrowPath.lineTo(center + halfArrowSize, center + halfArrowSize)
                arrowPath.close()
            }
            ArrowDirection.TOP -> {
                arrowPath.moveTo(center - halfArrowSize, center + halfArrowSize)
                arrowPath.lineTo(center, center - halfArrowSize)
                arrowPath.lineTo(center + halfArrowSize, center + halfArrowSize)
                arrowPath.close()
            }
            ArrowDirection.RIGHT -> {
                arrowPath.moveTo(center - halfArrowSize, center - halfArrowSize)
                arrowPath.lineTo(center + halfArrowSize, center)
                arrowPath.lineTo(center - halfArrowSize, center + halfArrowSize)
                arrowPath.close()
            }
            ArrowDirection.BOTTOM -> {
                arrowPath.moveTo(center - halfArrowSize, center - halfArrowSize)
                arrowPath.lineTo(center, center + halfArrowSize)
                arrowPath.lineTo(center + halfArrowSize, center - halfArrowSize)
                arrowPath.close()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Draw button background
        paint.color = MenuDesign.Colors.BUTTON_BACKGROUND
        canvas.drawRoundRect(
            0f, 0f, buttonSize, buttonSize,
            MenuDesign.Measurements.BUTTON_CORNER_RADIUS, MenuDesign.Measurements.BUTTON_CORNER_RADIUS,
            paint
        )

        // Draw arrow
        paint.color = MenuDesign.Colors.BUTTON_TEXT
        canvas.drawPath(arrowPath, paint)
    }

    enum class ArrowDirection {
        LEFT, TOP, RIGHT, BOTTOM
    }
}
