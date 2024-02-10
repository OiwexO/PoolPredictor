package com.iwex.poolpredictor.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import com.iwex.poolpredictor.presentation.resource.Colors
import com.iwex.poolpredictor.presentation.resource.Dimensions

//TODO refactor ArrowButton
class ArrowButton @JvmOverloads constructor(
    context: Context,
    arrowDirection: ArrowDirection = ArrowDirection.LEFT,
    size: Int = Dimensions.getInstance(context).arrowButtonSizePx
) : View(context) {
    private val buttonSize: Float = size.coerceAtLeast(0).toFloat()
    private val arrowSize: Float = buttonSize * 0.5f
    private val cornerRadius: Float = Dimensions.getInstance(context).buttonCornerRadiusPx

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
        paint.color = Colors.BUTTON_BACKGROUND
        canvas.drawRoundRect(
            0f,
            0f,
            buttonSize,
            buttonSize,
            cornerRadius,
            cornerRadius,
            paint
        )
        paint.color = Colors.BUTTON_TEXT
        canvas.drawPath(arrowPath, paint)
    }

    enum class ArrowDirection {
        LEFT, TOP, RIGHT, BOTTOM
    }
}
