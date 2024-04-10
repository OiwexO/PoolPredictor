package com.iwex.poolpredictor.presentation.view.tablePosition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.view.ArrowButton
import com.iwex.poolpredictor.presentation.viewmodel.tablePosition.TablePositionSetupViewModel
import com.iwex.poolpredictor.util.Utils

@SuppressLint("ViewConstructor")
class TablePositionSetupView(
    context: Context,
    private val viewModel: TablePositionSetupViewModel
) : ConstraintLayout(context) {

    val layoutParams: WindowManager.LayoutParams

    var onTablePositionSetListener: OnTablePositionSetListener? = null

    private val buttonLeft: ArrowButton
    private val buttonTop: ArrowButton
    private val buttonRight: ArrowButton
    private val buttonBottom: ArrowButton
    private val buttonReset: Button
    private val buttonSave: Button

    private val buttonMargin = Dimensions.getInstance(context).buttonMarginPx

    init {
        val arrowButtonSize = Dimensions.getInstance(context).arrowButtonSizePx
        val arrowButtonCornerRadius = Dimensions.getInstance(context).buttonCornerRadiusPx
        buttonLeft = ArrowButton.left(context, arrowButtonSize, arrowButtonCornerRadius)
        buttonTop = ArrowButton.top(context, arrowButtonSize, arrowButtonCornerRadius)
        buttonRight = ArrowButton.right(context, arrowButtonSize, arrowButtonCornerRadius)
        buttonBottom = ArrowButton.bottom(context, arrowButtonSize, arrowButtonCornerRadius)
        buttonReset = MenuWidgetFactory.addButton(LABEL_RESET_BUTTON, true, context, this)
        buttonSave = MenuWidgetFactory.addButton(LABEL_SAVE_BUTTON,true, context, this)
        observeViewModel()
        layoutParams = initLayoutParams()
        setButtonIds()
        setButtonClickListeners()
        addView(buttonLeft)
        addView(buttonTop)
        addView(buttonRight)
        addView(buttonBottom)
        applyConstraints()
    }

    private fun observeViewModel() {
        viewModel.currentPointIndex.observeForever {
            buttonSave.text = String.format(LABEL_SAVE_BUTTON, it)
        }
        viewModel.isTableSet.observeForever {
            if (it) {
                onTablePositionSetListener?.onTablePositionSet()
                viewModel.onTableSet()
            }
        }
    }

    private fun setButtonIds() {
        buttonLeft.id = View.generateViewId()
        buttonTop.id = View.generateViewId()
        buttonRight.id = View.generateViewId()
        buttonBottom.id = View.generateViewId()
        buttonSave.id = View.generateViewId()
        buttonReset.id = View.generateViewId()
    }

    private fun setButtonClickListeners() {
        buttonLeft.setOnClickListener { viewModel.onButtonLeft() }
        buttonTop.setOnClickListener { viewModel.onButtonTop() }
        buttonRight.setOnClickListener { viewModel.onButtonRight() }
        buttonBottom.setOnClickListener { viewModel.onButtonBottom() }
        buttonSave.setOnClickListener { viewModel.onSaveButton() }
        buttonReset.setOnClickListener { viewModel.onResetButton() }
    }

    private fun initLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            0,
            0,
            Utils.overlayWindowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun applyConstraints() {
        with(ConstraintSet()) {
            clone(this@TablePositionSetupView)
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
            applyTo(this@TablePositionSetupView)
        }
    }

    companion object {

        private const val LABEL_SAVE_BUTTON = "Save %d"
        private const val LABEL_RESET_BUTTON = "Reset"
    }
}
