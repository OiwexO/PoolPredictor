package com.iwex.poolpredictor.di.factory

import android.content.Context
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetEspSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.table.ResetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveAimSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveEspSettingsUseCase
import com.iwex.poolpredictor.domain.usecase.native.GetPredictionDataUseCase
import com.iwex.poolpredictor.domain.usecase.native.SetTablePositionNativeUseCase
import com.iwex.poolpredictor.domain.usecase.native.UpdateAimSettingsNativeUseCase
import com.iwex.poolpredictor.domain.usecase.table.GetIsTableSetUseCase
import com.iwex.poolpredictor.domain.usecase.table.GetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.table.SaveTablePositionUseCase

class UseCaseFactory private constructor(context: Context){

    private val repositoryFactory = RepositoryFactory.getInstance(context)

    val getAimSettingsUseCase: GetAimSettingsUseCase by lazy {
        GetAimSettingsUseCase(repositoryFactory.menuSettingsRepository)
    }

    val getEspSettingsUseCase: GetEspSettingsUseCase by lazy {
        GetEspSettingsUseCase(repositoryFactory.menuSettingsRepository)
    }

    val saveAimSettingsUseCase: SaveAimSettingsUseCase by lazy {
        SaveAimSettingsUseCase(repositoryFactory.menuSettingsRepository)
    }

    val saveEspSettingsUseCase: SaveEspSettingsUseCase by lazy {
        SaveEspSettingsUseCase(repositoryFactory.menuSettingsRepository)
    }

    val getPredictionDataUseCase: GetPredictionDataUseCase by lazy {
        GetPredictionDataUseCase(repositoryFactory.nativeRepository)
    }

    val setTablePositionNativeUseCase: SetTablePositionNativeUseCase by lazy {
        SetTablePositionNativeUseCase(repositoryFactory.nativeRepository)
    }

    val updateAimSettingsNativeUseCase: UpdateAimSettingsNativeUseCase by lazy {
        UpdateAimSettingsNativeUseCase(repositoryFactory.nativeRepository)
    }

    val getIsTableSetUseCase: GetIsTableSetUseCase by lazy {
        GetIsTableSetUseCase(repositoryFactory.tablePositionRepository)
    }

    val getTablePositionUseCase: GetTablePositionUseCase by lazy {
        GetTablePositionUseCase(repositoryFactory.tablePositionRepository)
    }

    val resetTablePositionUseCase: ResetTablePositionUseCase by lazy {
        ResetTablePositionUseCase(repositoryFactory.tablePositionRepository)
    }

    val saveTablePositionUseCase: SaveTablePositionUseCase by lazy {
        SaveTablePositionUseCase(repositoryFactory.tablePositionRepository)
    }

    companion object {

        @Volatile
        private var instance: UseCaseFactory? = null

        fun getInstance(context: Context): UseCaseFactory {
            return instance ?: synchronized(this) {
                instance ?: UseCaseFactory(context).also { instance = it }
            }
        }
    }

}