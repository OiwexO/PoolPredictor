package com.iwex.poolpredictor.presentation.viewmodel.tablePosition

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.usecase.table.GetIsTableSetUseCase
import com.iwex.poolpredictor.domain.usecase.table.GetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.table.SaveTablePositionUseCase
import kotlin.math.roundToInt

class TablePositionSharedViewModel(
    displayHeight: Int,
    displayWidth: Int,
    private val getIsTableSetUseCase: GetIsTableSetUseCase,
    private val getTablePositionUseCase: GetTablePositionUseCase,
    private val saveTablePositionUseCase: SaveTablePositionUseCase
) : ViewModel(), TablePositionSetupViewModel, TableShapeViewModel {

    private val defaultTablePosition: TablePosition
    private var currentTablePosition: TablePosition
    private var isFirstPointSet = false

    private val _isTableSet: MutableLiveData<Boolean> = MutableLiveData(false)
    override val isTableSet: LiveData<Boolean> get() = _isTableSet

    private val _tablePosition: MutableLiveData<TablePosition>
    override val tablePosition: LiveData<TablePosition> get() = _tablePosition

    private val _currentPointIndex = MutableLiveData(POINT_FIRST)
    override val currentPointIndex: LiveData<Int> get() = _currentPointIndex

    init {
        val isTableSet = getIsTableSetUseCase()
        defaultTablePosition = if (isTableSet) {
            getTablePositionUseCase()
        } else {
            initDefaultTablePosition(displayHeight, displayWidth)
        }
        currentTablePosition = defaultTablePosition.copy()
        _tablePosition = MutableLiveData(currentTablePosition)
        _isTableSet.postValue(isTableSet)
    }

    override fun onButtonLeft() {
        updateTablePositionByDirection(-1, 0)
    }

    override fun onButtonTop() {
        updateTablePositionByDirection(0, -1)
    }

    override fun onButtonRight() {
        updateTablePositionByDirection(1, 0)
    }

    override fun onButtonBottom() {
        updateTablePositionByDirection(0, 1)
    }

    override fun onSaveButton() {
        with(currentTablePosition) {
            if (this.left < this.right && this.top < this.bottom) {
                if (isFirstPointSet) {
                    _isTableSet.value = true
                } else {
                    isFirstPointSet = true
                    _currentPointIndex.value = POINT_SECOND
                }
            }
        }
    }

    override fun onResetButton() {
        isFirstPointSet = false
        currentTablePosition = defaultTablePosition.copy()
        _tablePosition.value = currentTablePosition
        _currentPointIndex.value = POINT_FIRST
    }

    override fun onTableSet() {
        saveTablePositionUseCase(currentTablePosition)
    }

    private fun initDefaultTablePosition(displayHeight: Int, displayWidth: Int): TablePosition {
        val defaultLeft = (displayWidth * 0.1697416974169742).roundToInt().toFloat()
        val defaultTop = (displayHeight * 0.2370370370370370).roundToInt().toFloat()
        val defaultRight = (displayWidth * 0.8427121771217713).roundToInt().toFloat()
        val defaultBottom = (displayHeight * 0.9120370370370371).roundToInt().toFloat()
        return TablePosition(defaultLeft, defaultTop, defaultRight, defaultBottom)
    }

    private fun updateTablePositionByDirection(deltaX: Int, deltaY: Int) {
        currentTablePosition = if (isFirstPointSet) {
            currentTablePosition.copy(right = currentTablePosition.right + deltaX, bottom = currentTablePosition.bottom + deltaY)
        } else {
            currentTablePosition.copy(left = currentTablePosition.left + deltaX, top = currentTablePosition.top + deltaY)
        }
        _tablePosition.value = currentTablePosition
    }

    companion object {

        private const val POINT_FIRST = 1
        private const val POINT_SECOND = 2
    }

}