package com.iwex.poolpredictor.di.factory

import android.content.Context
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.viewmodel.AimTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.EspTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.TablePositionViewModel

class ViewModelFactory private constructor(context: Context) {

    private val useCaseFactory = UseCaseFactory.getInstance(context)

    val aimTabViewModel: AimTabViewModel by lazy {
        AimTabViewModel(useCaseFactory.getAimTabStateUseCase, useCaseFactory.saveAimTabStateUseCase)
    }

    val espTabViewModel: EspTabViewModel by lazy {
        EspTabViewModel(
            useCaseFactory.getEspTabStateUseCase,
            useCaseFactory.saveEspTabStateUseCase,
            useCaseFactory.resetTablePositionUseCase
        )
    }

    val tablePositionViewModel: TablePositionViewModel by lazy {
        TablePositionViewModel(
            Dimensions.getInstance(context).displayHeight,
            Dimensions.getInstance(context).displayWidth,
            useCaseFactory.getIsTableSetUseCase,
            useCaseFactory.getTablePositionUseCase,
            useCaseFactory.saveTablePositionUseCase
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