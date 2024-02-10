package com.iwex.poolpredictor.domain.usecase.table

import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class GetIsTableSetUseCase(private val repository: TablePositionRepository) {

    operator fun invoke(): Boolean = repository.getIsTableSet()
}