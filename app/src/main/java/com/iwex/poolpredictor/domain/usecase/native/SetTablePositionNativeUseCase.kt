package com.iwex.poolpredictor.domain.usecase.native

import com.iwex.poolpredictor.domain.repository.NativeRepository
import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class SetTablePositionNativeUseCase(
    private val tablePositionRepository: TablePositionRepository,
    private val nativeRepository: NativeRepository,
) {
    operator fun invoke() {
        val tablePosition = tablePositionRepository.getTablePosition()
        nativeRepository.setTablePosition(tablePosition)
    }
}