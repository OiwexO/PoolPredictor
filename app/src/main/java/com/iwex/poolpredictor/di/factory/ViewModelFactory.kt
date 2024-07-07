package com.iwex.poolpredictor.di.factory

import android.app.Application
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.viewmodel.AimTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.LauncherViewModel
import com.iwex.poolpredictor.presentation.viewmodel.OtherTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.esp.EspSharedViewModel
import com.iwex.poolpredictor.presentation.viewmodel.tablePosition.TablePositionSharedViewModel

class ViewModelFactory private constructor(application: Application) {
    private val useCaseFactory = UseCaseFactory.getInstance(application)

    val launcherViewModel: LauncherViewModel by lazy {
        LauncherViewModel(useCaseFactory.getIsTableSetUseCase)
    }

    val aimTabViewModel: AimTabViewModel by lazy {
        AimTabViewModel(
            useCaseFactory.getAimSettingsUseCase,
            useCaseFactory.updateAimSettingsNativeUseCase,
            useCaseFactory.saveAimSettingsUseCase
        )
    }

    val espSharedViewModel: EspSharedViewModel by lazy {
        EspSharedViewModel(
            useCaseFactory.getEspSettingsUseCase,
            useCaseFactory.saveEspSettingsUseCase,
            useCaseFactory.resetTablePositionUseCase,
            useCaseFactory.getShotResultUseCase
        )
    }

    val otherTabViewModel: OtherTabViewModel by lazy {
        OtherTabViewModel(useCaseFactory.exitNativeUseCase)
    }

    val tablePositionSharedViewModel: TablePositionSharedViewModel by lazy {
        TablePositionSharedViewModel(
            Dimensions.getInstance(application).displayHeight,
            Dimensions.getInstance(application).displayWidth,
            useCaseFactory.saveTablePositionUseCase,
        )
    }

    companion object {
        private var instance: ViewModelFactory? = null

        fun getInstance(application: Application) = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(application).also { instance = it }
        }
    }
}
