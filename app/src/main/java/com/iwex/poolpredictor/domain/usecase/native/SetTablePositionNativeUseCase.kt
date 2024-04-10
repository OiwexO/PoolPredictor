package com.iwex.poolpredictor.domain.usecase.native

import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.domain.repository.NativeRepository

class SetTablePositionNativeUseCase(private val repository: NativeRepository) {

    operator fun invoke(tablePosition: TablePosition) = repository.setTablePosition(tablePosition)
}