package com.iwex.poolpredictor.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import com.iwex.poolpredictor.presentation.resource.MenuColors

@SuppressLint("ViewConstructor")
class ArrowButton private constructor(
    context: Context,
    private val size: Float,
    private val cornerRadius: Float,
    arrowDirection: ArrowDirection
) : View(context) {

    private val paint = Paint()

    private val arrowPath = Path()

    init {
        initPaint()
        initPath(arrowDirection)
    }

    private fun initPaint() {
        paint.apply {
            isAntiAlias = true
            style = Paint.Style.FILL
            strokeCap = Paint.Cap.ROUND
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureSpec = MeasureSpec.makeMeasureSpec(size.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(measureSpec, measureSpec)
    }

    private fun initPath(arrowDirection: ArrowDirection) {
        val center = size / 2f
        val halfArrowSize = size / 4f
        arrowPath.reset()
        arrowPath.moveTo(center - halfArrowSize, center)
        arrowPath.lineTo(center + halfArrowSize, center - halfArrowSize)
        arrowPath.lineTo(center + halfArrowSize, center + halfArrowSize)
        when (arrowDirection) {
            ArrowDirection.LEFT -> {}
            ArrowDirection.TOP -> arrowPath.rotate(90f, center, center)
            ArrowDirection.RIGHT -> arrowPath.rotate(180f, center, center)
            ArrowDirection.BOTTOM -> arrowPath.rotate(270f, center, center)
        }
        arrowPath.close()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = MenuColors.BUTTON_BACKGROUND
        canvas.drawRoundRect(
            0f,
            0f,
            size,
            size,
            cornerRadius,
            cornerRadius,
            paint
        )
        paint.color = MenuColors.BUTTON_TEXT
        canvas.drawPath(arrowPath, paint)
    }

    private enum class ArrowDirection {
        LEFT, TOP, RIGHT, BOTTOM
    }

    private fun Path.rotate(degrees: Float, px: Float, py: Float) {
        val matrix = android.graphics.Matrix()
        matrix.setRotate(degrees, px, py)
        this.transform(matrix)
    }

    companion object {

        fun left(context: Context, size: Float, cornerRadius: Float): ArrowButton =
            ArrowButton(context, size, cornerRadius, ArrowDirection.LEFT)

        fun top(context: Context, size: Float, cornerRadius: Float): ArrowButton =
            ArrowButton(context, size, cornerRadius, ArrowDirection.TOP)

        fun right(context: Context, size: Float, cornerRadius: Float): ArrowButton =
            ArrowButton(context, size, cornerRadius, ArrowDirection.RIGHT)

        fun bottom(context: Context, size: Float, cornerRadius: Float): ArrowButton =
            ArrowButton(context, size, cornerRadius, ArrowDirection.BOTTOM)
    }
}
