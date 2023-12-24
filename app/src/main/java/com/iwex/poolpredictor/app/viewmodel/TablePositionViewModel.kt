package com.iwex.poolpredictor.app.viewmodel

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.app.model.TablePosition
import com.iwex.poolpredictor.app.repository.TablePositionRepository
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.interfaces.OnButtonClickListener
import kotlin.math.roundToInt

class TablePositionViewModel(private val repository: TablePositionRepository): ViewModel() {
    val tableRect: LiveData<RectF> get() = _tableRect
    val isTableSet: Boolean get() = repository.getIsTableSet()
    val tablePosition: TablePosition get() = repository.getTablePosition()
    val pointNumber: Int get() = if (isTopLeftPointSet) 2 else 1
    private var onButtonClickListener: OnButtonClickListener? = null
    private val defaultTableRect: TablePosition
    private val _tableRect = MutableLiveData<RectF>()
    private var isTopLeftPointSet = false
    private var left: Int
    private var top: Int
    private var right: Int
    private var bottom: Int

    init {
        val displayWidth = MenuDesign.displayWidth
        val displayHeight = MenuDesign.displayHeight
        val defaultLeft = (displayWidth * 0.1697416974169742).roundToInt()    // width * myTableLeft / myScreenWidth
        val defaultTop = (displayHeight * 0.2370370370370370).roundToInt()    // height * myTableTop / myScreenHeight
        val defaultRight = (displayWidth * 0.8427121771217713).roundToInt()   // width * myTableRight / myScreenWidth
        val defaultBottom = (displayHeight * 0.9120370370370371).roundToInt() // height * myTableBottom / myScreenHeight
        defaultTableRect = TablePosition(defaultLeft, defaultTop, defaultRight, defaultBottom)
        left = defaultLeft
        top = defaultTop
        right = defaultRight
        bottom = defaultBottom
        updateTableRect()
    }

    fun decreaseX() {
        if (isTopLeftPointSet) right-- else left--
        updateTableRect()
    }

    fun increaseX() {
        if (isTopLeftPointSet) right++ else left++
        updateTableRect()
    }

    fun decreaseY() {
        if (isTopLeftPointSet) bottom-- else top--
        updateTableRect()
    }

    fun increaseY() {
        if (isTopLeftPointSet) bottom++ else top++
        updateTableRect()
    }

    fun resetPosition() {
        isTopLeftPointSet = false
        left = defaultTableRect.left
        top = defaultTableRect.top
        right = defaultTableRect.right
        bottom = defaultTableRect.bottom
        updateTableRect()
    }

    fun savePosition() {
        if (right > left && bottom > top) {
            if (isTopLeftPointSet) {
                repository.putIsTableSet(true)
                repository.putTablePosition(TablePosition(left, top, right, bottom))
                onButtonClickListener?.onButtonClicked()
            } else {
                isTopLeftPointSet = true
            }
        }
    }

    private fun updateTableRect() {
        _tableRect.value = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    fun setOnButtonClickListener(listener: OnButtonClickListener) {
        this.onButtonClickListener = listener
    }

}