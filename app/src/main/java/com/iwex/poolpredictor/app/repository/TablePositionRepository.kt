package com.iwex.poolpredictor.app.repository

import com.iwex.poolpredictor.app.model.TablePosition

class TablePositionRepository(private val repository: ConfigRepository) {
    companion object {
        private const val KEY_IS_TABLE_SET = "is_table_set"
        private const val KEY_TABLE_LEFT = "table_left"
        private const val KEY_TABLE_TOP = "table_top"
        private const val KEY_TABLE_RIGHT = "table_right"
        private const val KEY_TABLE_BOTTOM = "table_bottom"
    }

    fun getIsTableSet(): Boolean {
        return repository.getBoolean(KEY_IS_TABLE_SET, false)
    }

    fun putIsTableSet(isSet: Boolean) {
        repository.putBoolean(KEY_IS_TABLE_SET, isSet)
    }

    fun getTablePosition(): TablePosition {
        val left = repository.getInt(KEY_TABLE_LEFT, 0)
        val top = repository.getInt(KEY_TABLE_TOP, 0)
        val right = repository.getInt(KEY_TABLE_RIGHT, 0)
        val bottom = repository.getInt(KEY_TABLE_BOTTOM, 0)
        return TablePosition(left, top, right, bottom)
    }

    fun putTablePosition(tablePosition: TablePosition) {
        val (left, top, right, bottom) = tablePosition
        repository.putInt(KEY_TABLE_LEFT, left)
        repository.putInt(KEY_TABLE_TOP, top)
        repository.putInt(KEY_TABLE_RIGHT, right)
        repository.putInt(KEY_TABLE_BOTTOM, bottom)
    }
}