package com.iwex.poolpredictor.domain.usecase.native

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.model.ShotResult
import com.iwex.poolpredictor.domain.repository.NativeRepository

class GetShotResultUseCase(private val repository: NativeRepository) {

    operator fun invoke(): LiveData<ShotResult> = repository.shotResult
}