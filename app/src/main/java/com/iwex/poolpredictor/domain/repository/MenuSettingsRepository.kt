package com.iwex.poolpredictor.domain.repository

import com.iwex.poolpredictor.domain.model.AimSettings
import com.iwex.poolpredictor.domain.model.EspSettings

interface MenuSettingsRepository {

    fun getAimSettings(): AimSettings

    fun putAimSettings(aimSettings: AimSettings)

    fun getEspSettings(): EspSettings

    fun putEspSettings(espSettings: EspSettings)

}