package com.iwex.poolpredictor.presentation.view.tablePosition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import com.iwex.poolpredictor.presentation.resource.EspColors
import com.iwex.poolpredictor.presentation.view.NonInteractiveOverlayView
import com.iwex.poolpredictor.presentation.viewmodel.tablePosition.TableShapeViewModel

@SuppressLint("ViewConstructor")
class TableShapeView(
    context: Context,
    private val viewModel: TableShapeViewModel
) : NonInteractiveOverlayView(context) {

    private var tableRect = RectF()
    private val tablePaint = Paint()

    init {
        initPaint()
        observeViewModel()
    }

    private fun initPaint() {
        tablePaint.apply {
            color = EspColors.TABLE_RECT_COLOR
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = PAINT_STROKE_WIDTH
        }
    }

    private fun observeViewModel() {
        viewModel.tablePosition.observeForever {
            this.tableRect = RectF(
                it.left + HALF_PAINT_STROKE_WIDTH,
                it.top + HALF_PAINT_STROKE_WIDTH,
                it.right - HALF_PAINT_STROKE_WIDTH,
                it.bottom - HALF_PAINT_STROKE_WIDTH
            )
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(tableRect, CORNER_RADIUS, CORNER_RADIUS, tablePaint)
    }

    companion object {

        private const val PAINT_STROKE_WIDTH = 20f
        private const val HALF_PAINT_STROKE_WIDTH = PAINT_STROKE_WIDTH / 2
        private const val CORNER_RADIUS = 20f
    }
}