package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.menu.tabs.EspTabState
import com.iwex.poolpredictor.domain.repository.MenuStateRepository

class SaveEspTabStateUseCase(private val repository: MenuStateRepository) {

    operator fun invoke(espTabState: EspTabState) {
        repository.putEspTabState(espTabState)
    }
}