package com.iwex.poolpredictor.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import com.iwex.poolpredictor.presentation.resource.MenuColors

@SuppressLint("ViewConstructor")
class ArrowButton(
    context: Context,
    private val size: Float,
    private val cornerRadius: Float,
    arrowDirection: ArrowDirection
) : View(context) {
    private val paint = initPaint()
    private val arrowPath = initPath(arrowDirection)

    private fun initPaint() = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeCap = Paint.Cap.ROUND
    }

    private fun initPath(arrowDirection: ArrowDirection): Path {
        val center = size / 2f
        val halfArrowSize = size / 4f
        val matrix = android.graphics.Matrix().apply {
            setRotate(arrowDirection.degrees, center, center)
        }
        return Path().apply {
            reset()
            moveTo(center - halfArrowSize, center)
            lineTo(center + halfArrowSize, center - halfArrowSize)
            lineTo(center + halfArrowSize, center + halfArrowSize)
            transform(matrix)
            close()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val measureSpec = MeasureSpec.makeMeasureSpec(size.toInt(), MeasureSpec.EXACTLY)
        super.onMeasure(measureSpec, measureSpec)
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

    enum class ArrowDirection(val degrees: Float) {
        LEFT(0f), TOP(90f), RIGHT(180f), BOTTOM(270f);
    }
}
