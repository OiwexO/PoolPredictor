package com.iwex.poolpredictor.domain.usecase.native

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.repository.NativeRepository

class GetPredictionDataUseCase(private val repository: NativeRepository) {

    operator fun invoke(): LiveData<FloatArray> = repository.predictionData
}