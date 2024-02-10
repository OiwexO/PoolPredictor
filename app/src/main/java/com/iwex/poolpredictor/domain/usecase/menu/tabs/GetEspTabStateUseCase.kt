package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.menu.tabs.EspTabState
import com.iwex.poolpredictor.domain.repository.MenuStateRepository

class GetEspTabStateUseCase(private val repository: MenuStateRepository) {

    operator fun invoke(): EspTabState = repository.getEspTabState()
}