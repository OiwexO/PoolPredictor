package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.repository.MenuSettingsRepository

class GetAimSettingsUseCase(private val repository: MenuSettingsRepository) {

    operator fun invoke(): AimSettings = repository.getAimSettings()
}