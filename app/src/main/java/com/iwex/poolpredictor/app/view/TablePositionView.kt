package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.MenuWidgetFactory
import com.iwex.poolpredictor.app.viewmodel.TablePositionViewModel

@SuppressLint("ViewConstructor")
class TablePositionView(
    context: Context,
    private val viewModel: TablePositionViewModel
    ) : ConstraintLayout(context) {

    private val buttonLeft = ArrowButton(
        context,
        MenuDesign.Measurements.ARROW_BUTTON_SIZE,
        ArrowButton.ArrowDirection.LEFT
    )

    private val buttonTop = ArrowButton(
        context,
        MenuDesign.Measurements.ARROW_BUTTON_SIZE,
        ArrowButton.ArrowDirection.TOP
    )

    private val buttonRight = ArrowButton(
        context,
        MenuDesign.Measurements.ARROW_BUTTON_SIZE,
        ArrowButton.ArrowDirection.RIGHT
    )

    private val buttonBottom = ArrowButton(
        context,
        MenuDesign.Measurements.ARROW_BUTTON_SIZE,
        ArrowButton.ArrowDirection.BOTTOM
    )

    private val buttonReset = MenuWidgetFactory.addButton(
        LABEL_RESET_BUTTON,
        true,
        context,
        this
    )

    private val buttonSave = MenuWidgetFactory.addButton(
        String.format(LABEL_SAVE_BUTTON, viewModel.pointNumber),
        true,
        context,
        this
    )

    private val saveButtonText: String
        get() = String.format(LABEL_SAVE_BUTTON, viewModel.pointNumber)

    companion object {
        private const val LABEL_SAVE_BUTTON = "Save %d"
        private const val LABEL_RESET_BUTTON = "Reset"
    }

    init {
        buttonLeft.setOnClickListener { viewModel.decreaseX() }
        buttonTop.setOnClickListener { viewModel.decreaseY() }
        buttonRight.setOnClickListener { viewModel.increaseX() }
        buttonBottom.setOnClickListener { viewModel.increaseY() }

        buttonSave.id = View.generateViewId()
        buttonSave.setOnClickListener {
            viewModel.savePosition()
            buttonSave.text = saveButtonText
        }

        buttonReset.id = View.generateViewId()
        buttonReset.setOnClickListener {
            viewModel.resetPosition()
            buttonSave.text = saveButtonText
        }

        addView(buttonLeft)
        addView(buttonTop)
        addView(buttonRight)
        addView(buttonBottom)
        applyConstraints()
    }

    private fun applyConstraints() {
        with(ConstraintSet()) {
            clone(this@TablePositionView)
            connect(buttonLeft.id, START, PARENT_ID, START)
            connect(buttonLeft.id, TOP, buttonTop.id, BOTTOM)
            connect(buttonLeft.id, END, buttonRight.id, START)
            setMargin(buttonLeft.id, END, MenuDesign.Measurements.BUTTON_MARGIN)
            connect(buttonTop.id, START, PARENT_ID, START)
            connect(buttonTop.id, TOP, PARENT_ID, TOP)
            connect(buttonTop.id, END, PARENT_ID, END)
            connect(buttonRight.id, START, buttonLeft.id, END)
            connect(buttonRight.id, TOP, buttonTop.id, BOTTOM)
            connect(buttonRight.id, END, PARENT_ID, END)
            setMargin(buttonRight.id, START, MenuDesign.Measurements.BUTTON_MARGIN)
            connect(buttonBottom.id, START, PARENT_ID, START)
            connect(buttonBottom.id, TOP, buttonLeft.id, BOTTOM)
            connect(buttonBottom.id, END, PARENT_ID, END)
            connect(buttonSave.id, START, buttonReset.id, END)
            connect(buttonSave.id, TOP, buttonBottom.id, BOTTOM)
            connect(buttonSave.id, END, PARENT_ID, END)
            setMargin(buttonSave.id, START, MenuDesign.Measurements.BUTTON_MARGIN)
            setMargin(buttonSave.id, TOP, MenuDesign.Measurements.BUTTON_MARGIN)
            connect(buttonReset.id, START, PARENT_ID, START)
            connect(buttonReset.id, TOP, buttonBottom.id, BOTTOM)
            connect(buttonReset.id, END, buttonSave.id, START)
            setMargin(buttonReset.id, TOP, MenuDesign.Measurements.BUTTON_MARGIN)
            setMargin(buttonReset.id, END, MenuDesign.Measurements.BUTTON_MARGIN)
            applyTo(this@TablePositionView)
        }
    }

}
