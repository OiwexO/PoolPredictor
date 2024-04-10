package com.iwex.poolpredictor.domain.usecase.table

import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class SaveTablePositionUseCase(private val repository: TablePositionRepository) {

    operator fun invoke(tablePosition: TablePosition) {
        repository.putTablePosition(tablePosition)
        repository.putIsTableSet(true)
    }
}