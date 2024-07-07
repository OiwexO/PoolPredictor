package com.iwex.poolpredictor.presentation.view.tablePosition

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.lifecycle.Observer
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.util.OverlayUtils
import com.iwex.poolpredictor.presentation.util.StringUtils.Companion.formatStringWithNumber
import com.iwex.poolpredictor.presentation.viewmodel.tablePosition.TablePositionSetupViewModel

@SuppressLint("ViewConstructor")
class TablePositionSetupView(
    context: Context,
    private val viewModel: TablePositionSetupViewModel,
    private val onTablePositionSetListener: OnTablePositionSetListener
) : ConstraintLayout(context) {
    val layoutParams = initLayoutParams()
    private val buttonLeft = MenuWidgetFactory.addArrowButtonLeft(context, this)
    private val buttonTop = MenuWidgetFactory.addArrowButtonTop(context, this)
    private val buttonRight = MenuWidgetFactory.addArrowButtonRight(context, this)
    private val buttonBottom = MenuWidgetFactory.addArrowButtonBottom(context, this)
    private val buttonReset =
        MenuWidgetFactory.addButton(Strings.LABEL_RESET_BUTTON, true, context, this)
    private val buttonSave =
        MenuWidgetFactory.addButton(Strings.LABEL_SAVE_BUTTON, true, context, this)

    private val currentPointIndexObserver = Observer<Int> {
        buttonSave.text = formatStringWithNumber(Strings.LABEL_SAVE_BUTTON, it)
    }
    private val isTableSetObserver = Observer<Boolean> {
        if (it) {
            viewModel.onTableSet()
            onTablePositionSetListener.onTablePositionSet()
        }
    }

    init {
        setButtonIds()
        setButtonClickListeners()
        applyConstraints()
        observeViewModel()
    }

    private fun initLayoutParams(): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            0,
            0,
            OverlayUtils.overlayWindowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
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

    private fun applyConstraints() {
        val buttonMargin = Dimensions.getInstance(context).buttonMarginPx
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

    private fun observeViewModel() {
        viewModel.currentPointIndex.observeForever(currentPointIndexObserver)
        viewModel.isTableSet.observeForever(isTableSetObserver)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.currentPointIndex.removeObserver(currentPointIndexObserver)
        viewModel.isTableSet.removeObserver(isTableSetObserver)
    }
}
