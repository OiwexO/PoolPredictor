package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.MenuWidgetFactory
import com.iwex.poolpredictor.app.viewmodel.TablePositionViewModel

@SuppressLint("ViewConstructor")
class TablePositionView(context: Context, private val viewModel: TablePositionViewModel) :
    ConstraintLayout(context) {
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
    private val buttonReset = MenuWidgetFactory.addButton(LABEL_RESET_BUTTON, true, context, this)
    private val buttonSave = MenuWidgetFactory.addButton(
        String.format(LABEL_SAVE_BUTTON, viewModel.getPointNumber()),
        true,
        context,
        this
    )

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
            buttonSave.text = String.format(LABEL_SAVE_BUTTON, viewModel.getPointNumber())
        }

        buttonReset.id = View.generateViewId()
        buttonReset.setOnClickListener {
            viewModel.resetPosition()
            buttonSave.text = String.format(LABEL_SAVE_BUTTON, viewModel.getPointNumber())
        }

        addView(buttonLeft)
        addView(buttonTop)
        addView(buttonRight)
        addView(buttonBottom)

        applyConstraints()
    }

    private fun applyConstraints() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        constraintSet.connect(
            buttonLeft.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(buttonLeft.id, ConstraintSet.TOP, buttonTop.id, ConstraintSet.BOTTOM)
        constraintSet.connect(buttonLeft.id, ConstraintSet.END, buttonRight.id, ConstraintSet.START)
        constraintSet.setMargin(
            buttonLeft.id,
            ConstraintSet.END,
            MenuDesign.Measurements.BUTTON_MARGIN
        )

        constraintSet.connect(
            buttonTop.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            buttonTop.id,
            ConstraintSet.TOP,
            ConstraintSet.PARENT_ID,
            ConstraintSet.TOP
        )
        constraintSet.connect(
            buttonTop.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )

        constraintSet.connect(buttonRight.id, ConstraintSet.START, buttonLeft.id, ConstraintSet.END)
        constraintSet.connect(buttonRight.id, ConstraintSet.TOP, buttonTop.id, ConstraintSet.BOTTOM)
        constraintSet.connect(
            buttonRight.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.setMargin(
            buttonRight.id,
            ConstraintSet.START,
            MenuDesign.Measurements.BUTTON_MARGIN
        )

        constraintSet.connect(
            buttonBottom.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            buttonBottom.id,
            ConstraintSet.TOP,
            buttonLeft.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            buttonBottom.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )

        constraintSet.connect(buttonSave.id, ConstraintSet.START, buttonReset.id, ConstraintSet.END)
        constraintSet.connect(
            buttonSave.id,
            ConstraintSet.TOP,
            buttonBottom.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(
            buttonSave.id,
            ConstraintSet.END,
            ConstraintSet.PARENT_ID,
            ConstraintSet.END
        )
        constraintSet.setMargin(
            buttonSave.id,
            ConstraintSet.START,
            MenuDesign.Measurements.BUTTON_MARGIN
        )
        constraintSet.setMargin(
            buttonSave.id,
            ConstraintSet.TOP,
            MenuDesign.Measurements.BUTTON_MARGIN
        )

        constraintSet.connect(
            buttonReset.id,
            ConstraintSet.START,
            ConstraintSet.PARENT_ID,
            ConstraintSet.START
        )
        constraintSet.connect(
            buttonReset.id,
            ConstraintSet.TOP,
            buttonBottom.id,
            ConstraintSet.BOTTOM
        )
        constraintSet.connect(buttonReset.id, ConstraintSet.END, buttonSave.id, ConstraintSet.START)
        constraintSet.setMargin(
            buttonReset.id,
            ConstraintSet.TOP,
            MenuDesign.Measurements.BUTTON_MARGIN
        )
        constraintSet.setMargin(
            buttonReset.id,
            ConstraintSet.END,
            MenuDesign.Measurements.BUTTON_MARGIN
        )

        constraintSet.applyTo(this)
    }

}
