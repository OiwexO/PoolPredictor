package com.iwex.poolpredictor.domain.usecase.native

import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.repository.NativeRepository

class UpdateAimSettingsNativeUseCase(private val repository: NativeRepository) {

    operator fun invoke(aimSettings: AimSettings) = repository.updateAimSettings(aimSettings)
}