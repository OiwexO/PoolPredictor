package com.iwex.poolpredictor.app.viewmodel

import android.content.Context
import com.iwex.poolpredictor.app.repository.RepositoryFactory

class ViewModelFactory private constructor(context: Context) {
    companion object {
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory {
//            val appContext = context.applicationContext
            return instance ?: synchronized(this) {
                instance ?: ViewModelFactory(context).also { instance = it }
            }
        }

    }

    private val repositoryFactory = RepositoryFactory.getInstance(context)

    val aimTabViewModel: AimTabViewModel by lazy {
        AimTabViewModel(repositoryFactory.aimTabRepository)
    }

    val espTabViewModel: EspTabViewModel by lazy {
        EspTabViewModel(repositoryFactory.espTabRepository, repositoryFactory.tablePositionRepository, espViewModel)
    }

    val espViewModel: EspViewModel by lazy {
        EspViewModel()
    }

    val tablePositionViewModel: TablePositionViewModel by lazy {
        TablePositionViewModel(repositoryFactory.tablePositionRepository)
    }

}