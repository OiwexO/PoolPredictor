package com.iwex.poolpredictor.domain.usecase.menu.tabs

import com.iwex.poolpredictor.domain.model.menu.tabs.AimTabState
import com.iwex.poolpredictor.domain.repository.MenuStateRepository

class SaveAimTabStateUseCase(private val repository: MenuStateRepository) {

    operator fun invoke(aimTabState: AimTabState) {
        repository.putAimTabState(aimTabState)
    }
}