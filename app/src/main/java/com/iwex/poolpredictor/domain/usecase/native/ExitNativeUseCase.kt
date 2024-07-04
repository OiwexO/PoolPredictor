package com.iwex.poolpredictor.domain.usecase.native

import com.iwex.poolpredictor.domain.repository.NativeRepository

class ExitNativeUseCase(private val repository: NativeRepository) {

    operator fun invoke() = repository.exit()
}