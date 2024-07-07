package com.iwex.poolpredictor.presentation.view.tablePosition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import androidx.lifecycle.Observer
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.presentation.resource.EspColors
import com.iwex.poolpredictor.presentation.view.NonInteractiveOverlayView
import com.iwex.poolpredictor.presentation.viewmodel.tablePosition.TableShapeViewModel

@SuppressLint("ViewConstructor")
class TableShapeView(
    context: Context,
    private val viewModel: TableShapeViewModel
) : NonInteractiveOverlayView(context) {
    private var tableRect = RectF()
    private val tablePaint = initTablePaint()
    private val tablePositionObserver = Observer<TablePosition> {
        this.tableRect = RectF(
            it.left + HALF_PAINT_STROKE_WIDTH,
            it.top + HALF_PAINT_STROKE_WIDTH,
            it.right - HALF_PAINT_STROKE_WIDTH,
            it.bottom - HALF_PAINT_STROKE_WIDTH
        )
        invalidate()
    }

    init {
        observeViewModel()
    }

    private fun initTablePaint() = Paint().apply {
        color = EspColors.TABLE_RECT_COLOR
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = PAINT_STROKE_WIDTH
    }

    private fun observeViewModel() {
        viewModel.tablePosition.observeForever(tablePositionObserver)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(tableRect, CORNER_RADIUS, CORNER_RADIUS, tablePaint)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.tablePosition.removeObserver(tablePositionObserver)
    }

    companion object {
        private const val PAINT_STROKE_WIDTH = 20f
        private const val HALF_PAINT_STROKE_WIDTH = PAINT_STROKE_WIDTH / 2
        private const val CORNER_RADIUS = 20f
    }
}
