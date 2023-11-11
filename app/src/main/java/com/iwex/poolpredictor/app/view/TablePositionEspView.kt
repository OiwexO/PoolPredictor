package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.view.View
import com.iwex.poolpredictor.app.util.EspColors
import com.iwex.poolpredictor.app.viewmodel.TablePositionViewModel

@SuppressLint("ViewConstructor")
class TablePositionEspView(context: Context, private val viewModel: TablePositionViewModel) : View(context) {
    private lateinit var tableShapePath: Path
    private var tableRect = RectF()
    private val tablePaint: Paint = Paint().apply {
        color = EspColors.BALLS_COLORS[0]
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 1f
    }

    init {
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.position.observeForever { position ->
            position?.let { tablePosition ->
                tableShapePath = viewModel.getTableShapePath()
                tableRect = RectF(
                    tablePosition.left.toFloat(),
                    tablePosition.top.toFloat(),
                    tablePosition.right.toFloat(),
                    tablePosition.bottom.toFloat()
                )
                invalidate()
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(tableRect, 20f, 20f, tablePaint)
        canvas.drawPath(tableShapePath, tablePaint)
    }
}