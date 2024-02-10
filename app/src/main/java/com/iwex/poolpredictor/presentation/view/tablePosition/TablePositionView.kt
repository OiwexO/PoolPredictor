package com.iwex.poolpredictor.presentation.view.tablePosition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.*
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.view.ArrowButton
import com.iwex.poolpredictor.presentation.viewmodel.TablePositionViewModel

//TODO Refactor TablePositionView
@SuppressLint("ViewConstructor")
class TablePositionView(
    context: Context,
    private val viewModel: TablePositionViewModel
) : ConstraintLayout(context) {

    val layoutParams: WindowManager.LayoutParams

    var onTablePositionSavedListener: OnTablePositionSavedListener? = null

    private val buttonLeft = ArrowButton(
        context,
        ArrowButton.ArrowDirection.LEFT
    )
    private val buttonTop = ArrowButton(
        context,
        ArrowButton.ArrowDirection.TOP
    )
    private val buttonRight = ArrowButton(
        context,
        ArrowButton.ArrowDirection.RIGHT
    )
    private val buttonBottom = ArrowButton(
        context,
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
    private val buttonMargin = Dimensions.getInstance(context).buttonMarginPx

    private val saveButtonText: String
        get() = String.format(LABEL_SAVE_BUTTON, viewModel.pointNumber)

    companion object {
        private const val LABEL_SAVE_BUTTON = "Save %d"
        private const val LABEL_RESET_BUTTON = "Reset"
    }

    init {
        layoutParams = initLayoutParams()
        buttonLeft.setOnClickListener { viewModel.decreaseX() }
        buttonTop.setOnClickListener { viewModel.decreaseY() }
        buttonRight.setOnClickListener { viewModel.increaseX() }
        buttonBottom.setOnClickListener { viewModel.increaseY() }
        buttonSave.id = View.generateViewId()
        buttonSave.setOnClickListener {
            if (viewModel.savePosition()) {
                onTablePositionSavedListener?.onTablePositionSaved()
            }
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

    private fun initLayoutParams(): WindowManager.LayoutParams {
        val windowType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) TYPE_APPLICATION_OVERLAY else TYPE_APPLICATION
        return WindowManager.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            0,
            0,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun applyConstraints() {
        with(ConstraintSet()) {
            clone(this@TablePositionView)
            connect(buttonLeft.id, START, PARENT_ID, START)
            connect(buttonLeft.id, TOP, buttonTop.id, BOTTOM)
            connect(buttonLeft.id, END, buttonRight.id, START)
            setMargin(buttonLeft.id, END, buttonMargin)
            connect(buttonTop.id, START, PARENT_ID, START)
            connect(buttonTop.id, TOP, PARENT_ID, TOP)
            connect(buttonTop.id, END, PARENT_ID, END)
            connect(buttonRight.id, START, buttonLeft.id, END)
            connect(buttonRight.id, TOP, buttonTop.id, BOTTOM)
            connect(buttonRight.id, END, PARENT_ID, END)
            setMargin(buttonRight.id, START, buttonMargin)
            connect(buttonBottom.id, START, PARENT_ID, START)
            connect(buttonBottom.id, TOP, buttonLeft.id, BOTTOM)
            connect(buttonBottom.id, END, PARENT_ID, END)
            connect(buttonSave.id, START, buttonReset.id, END)
            connect(buttonSave.id, TOP, buttonBottom.id, BOTTOM)
            connect(buttonSave.id, END, PARENT_ID, END)
            setMargin(buttonSave.id, START, buttonMargin)
            setMargin(buttonSave.id, TOP, buttonMargin)
            connect(buttonReset.id, START, PARENT_ID, START)
            connect(buttonReset.id, TOP, buttonBottom.id, BOTTOM)
            connect(buttonReset.id, END, buttonSave.id, START)
            setMargin(buttonReset.id, TOP, buttonMargin)
            setMargin(buttonReset.id, END, buttonMargin)
            applyTo(this@TablePositionView)
        }
    }

}
