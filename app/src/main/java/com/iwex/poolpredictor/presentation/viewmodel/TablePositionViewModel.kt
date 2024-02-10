package com.iwex.poolpredictor.presentation.viewmodel

import android.graphics.RectF
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.usecase.table.GetIsTableSetUseCase
import com.iwex.poolpredictor.domain.usecase.table.GetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.table.SaveTablePositionUseCase
import kotlin.math.roundToInt

class TablePositionViewModel(
    displayHeight: Int,
    displayWidth: Int,
    private val getIsTableSetUseCase: GetIsTableSetUseCase,
    private val getTablePositionUseCase: GetTablePositionUseCase,
    private val saveTablePositionUseCase: SaveTablePositionUseCase
): ViewModel() {
    val tableRect: LiveData<RectF> get() = _tableRect
    val isTableSet: Boolean get() = getIsTableSetUseCase()
    val tablePosition: TablePosition get() = getTablePositionUseCase()
    val pointNumber: Int get() = if (isTopLeftPointSet) 2 else 1
    private val defaultTableRect: TablePosition
    private val _tableRect = MutableLiveData<RectF>()
    private var isTopLeftPointSet = false
    private var left: Int
    private var top: Int
    private var right: Int
    private var bottom: Int

    init {
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

    fun savePosition(): Boolean {
        if (right > left && bottom > top) {
            if (isTopLeftPointSet) {
                saveTablePositionUseCase(TablePosition(left, top, right, bottom))
                return true
            } else {
                isTopLeftPointSet = true
            }
        }
        return false
    }

    private fun updateTableRect() {
        _tableRect.value = RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

}