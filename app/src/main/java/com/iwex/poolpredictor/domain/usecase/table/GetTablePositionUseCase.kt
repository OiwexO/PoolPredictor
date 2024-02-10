package com.iwex.poolpredictor.domain.usecase.table

import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class GetTablePositionUseCase(private val repository: TablePositionRepository) {

    operator fun invoke(): TablePosition = repository.getTablePosition()
}