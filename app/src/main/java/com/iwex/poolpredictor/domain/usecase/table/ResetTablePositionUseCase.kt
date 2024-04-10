package com.iwex.poolpredictor.domain.usecase.table

import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class ResetTablePositionUseCase(private val repository: TablePositionRepository) {

    operator fun invoke() {
        repository.putIsTableSet(false)
    }
}