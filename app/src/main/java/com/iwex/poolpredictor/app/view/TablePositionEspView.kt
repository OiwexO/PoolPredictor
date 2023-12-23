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
        style = Paint.Style.FILL
    }

    init {
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.tableRect.observeForever { tableRect ->
            this.tableRect = tableRect
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRoundRect(tableRect, 20f, 20f, tablePaint)
    }
}