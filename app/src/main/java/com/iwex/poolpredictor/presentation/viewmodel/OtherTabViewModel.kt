package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.usecase.native.ExitNativeUseCase

class OtherTabViewModel(private val exitNativeUseCase: ExitNativeUseCase) : ViewModel(), BaseMenuTabViewModel {
    fun onExit() {
        exitNativeUseCase()
    }

    override fun saveSettings() {}
}