package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.EspSettings
import com.iwex.poolpredictor.domain.repository.MenuSettingsRepository

class SaveEspSettingsUseCase(private val repository: MenuSettingsRepository) {
    operator fun invoke(espSettings: EspSettings) = repository.putEspSettings(espSettings)
}
