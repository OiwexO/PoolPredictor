package com.iwex.poolpredictor.di.factory

import android.content.Context
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.viewmodel.AimTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.esp.EspSharedViewModel
import com.iwex.poolpredictor.presentation.viewmodel.tablePosition.TablePositionSharedViewModel

class ViewModelFactory private constructor(context: Context) {

    private val useCaseFactory = UseCaseFactory.getInstance(context)

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

    val tablePositionSharedViewModel: TablePositionSharedViewModel by lazy {
        TablePositionSharedViewModel(
            Dimensions.getInstance(context).displayHeight,
            Dimensions.getInstance(context).displayWidth,
            useCaseFactory.getIsTableSetUseCase,
            useCaseFactory.getTablePositionUseCase,
            useCaseFactory.saveTablePositionUseCase,
            useCaseFactory.setTablePositionNativeUseCase
        )
    }

    companion object {

        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory {
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context).also { instance = it }
            }
        }

    }

}