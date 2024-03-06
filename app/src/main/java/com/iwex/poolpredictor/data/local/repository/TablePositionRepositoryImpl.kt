package com.iwex.poolpredictor.data.local.repository

import android.content.SharedPreferences
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class TablePositionRepositoryImpl(
    private val preferences: SharedPreferences
) : TablePositionRepository {

    override fun getTablePosition(): TablePosition = with(preferences) {
        return TablePosition(
            left = getInt(KEY_TABLE_LEFT, TablePosition.DEFAULT.left),
            top = getInt(KEY_TABLE_TOP, TablePosition.DEFAULT.top),
            right = getInt(KEY_TABLE_RIGHT, TablePosition.DEFAULT.right),
            bottom = getInt(KEY_TABLE_BOTTOM, TablePosition.DEFAULT.bottom)
        )
    }

    override fun putTablePosition(tablePosition: TablePosition) {
        with(preferences.edit()) {
            putInt(KEY_TABLE_LEFT, tablePosition.left)
            putInt(KEY_TABLE_TOP, tablePosition.top)
            putInt(KEY_TABLE_RIGHT, tablePosition.right)
            putInt(KEY_TABLE_BOTTOM, tablePosition.bottom)
            apply()
        }
    }

    override fun getIsTableSet(): Boolean = preferences.getBoolean(KEY_IS_TABLE_SET, false)

    override fun putIsTableSet(isTableSet: Boolean) {
        preferences.edit().putBoolean(KEY_IS_TABLE_SET, isTableSet).apply()
    }

    companion object {

        private const val KEY_IS_TABLE_SET = "is_table_set"
        private const val KEY_TABLE_LEFT = "table_left"
        private const val KEY_TABLE_TOP = "table_top"
        private const val KEY_TABLE_RIGHT = "table_right"
        private const val KEY_TABLE_BOTTOM = "table_bottom"
    }

}