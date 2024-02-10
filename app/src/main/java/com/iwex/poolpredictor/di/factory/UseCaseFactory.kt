package com.iwex.poolpredictor.di.factory

import android.content.Context
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetAimTabStateUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.GetEspTabStateUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.ResetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveAimTabStateUseCase
import com.iwex.poolpredictor.domain.usecase.menu.tabs.SaveEspTabStateUseCase
import com.iwex.poolpredictor.domain.usecase.table.GetIsTableSetUseCase
import com.iwex.poolpredictor.domain.usecase.table.GetTablePositionUseCase
import com.iwex.poolpredictor.domain.usecase.table.SaveTablePositionUseCase

class UseCaseFactory private constructor(context: Context){

    private val repositoryFactory = RepositoryFactory.getInstance(context)

    val getAimTabStateUseCase: GetAimTabStateUseCase by lazy {
        GetAimTabStateUseCase(repositoryFactory.menuStateRepository)
    }

    val getEspTabStateUseCase: GetEspTabStateUseCase by lazy {
        GetEspTabStateUseCase(repositoryFactory.menuStateRepository)
    }

    val resetTablePositionUseCase: ResetTablePositionUseCase by lazy {
        ResetTablePositionUseCase(repositoryFactory.tablePositionRepository)
    }

    val saveAimTabStateUseCase: SaveAimTabStateUseCase by lazy {
        SaveAimTabStateUseCase(repositoryFactory.menuStateRepository)
    }

    val saveEspTabStateUseCase: SaveEspTabStateUseCase by lazy {
        SaveEspTabStateUseCase(repositoryFactory.menuStateRepository)
    }

    val getIsTableSetUseCase: GetIsTableSetUseCase by lazy {
        GetIsTableSetUseCase(repositoryFactory.tablePositionRepository)
    }

    val getTablePositionUseCase: GetTablePositionUseCase by lazy {
        GetTablePositionUseCase(repositoryFactory.tablePositionRepository)
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