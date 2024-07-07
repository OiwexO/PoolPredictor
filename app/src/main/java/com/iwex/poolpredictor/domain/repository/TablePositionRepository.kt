package com.iwex.poolpredictor.domain.repository

import com.iwex.poolpredictor.domain.model.TablePosition

interface TablePositionRepository {
    fun getTablePosition(): TablePosition

    fun putTablePosition(tablePosition: TablePosition)

    fun getIsTableSet(): Boolean

    fun putIsTableSet(isTableSet: Boolean)
}
