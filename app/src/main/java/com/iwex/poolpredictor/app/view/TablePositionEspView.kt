package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.iwex.poolpredictor.app.util.EspColors
import com.iwex.poolpredictor.app.viewmodel.TablePositionViewModel

@SuppressLint("ViewConstructor")
class TablePositionEspView(context: Context, private val viewModel: TablePositionViewModel) : View(context) {
    private var tableRect = RectF()
    private val tablePaint: Paint = Paint().apply {
        color = EspColors.TABLE_RECT_COLOR
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = PAINT_STROKE_WIDTH
    }

    init {
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.tableRect.observeForever { tableRect ->
            this.tableRect = RectF(
                tableRect.left + HALF_STROKE_WIDTH,
                tableRect.top + HALF_STROKE_WIDTH,
                tableRect.right - HALF_STROKE_WIDTH,
                tableRect.bottom - HALF_STROKE_WIDTH
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
        private const val HALF_STROKE_WIDTH = PAINT_STROKE_WIDTH / 2
        private const val CORNER_RADIUS = 20f

    }
}