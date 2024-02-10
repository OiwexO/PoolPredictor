package com.iwex.poolpredictor.domain.repository

import com.iwex.poolpredictor.domain.model.menu.tabs.AimTabState
import com.iwex.poolpredictor.domain.model.menu.tabs.EspTabState

interface MenuStateRepository {

    fun getAimTabState(): AimTabState

    fun putAimTabState(aimTabState: AimTabState)

    fun getEspTabState(): EspTabState

    fun putEspTabState(espTabState: EspTabState)

}