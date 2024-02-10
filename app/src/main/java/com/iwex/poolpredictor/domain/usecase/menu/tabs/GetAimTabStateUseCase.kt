package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.menu.tabs.AimTabState
import com.iwex.poolpredictor.domain.repository.MenuStateRepository

class GetAimTabStateUseCase(private val repository: MenuStateRepository) {

    operator fun invoke(): AimTabState = repository.getAimTabState()
}