package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.EspSettings
import com.iwex.poolpredictor.domain.repository.MenuSettingsRepository

class GetEspSettingsUseCase(private val repository: MenuSettingsRepository) {

    operator fun invoke(): EspSettings = repository.getEspSettings()
}