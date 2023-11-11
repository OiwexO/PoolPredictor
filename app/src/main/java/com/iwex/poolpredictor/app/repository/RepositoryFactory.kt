package com.iwex.poolpredictor.app.repository

import android.content.Context
import android.content.SharedPreferences

class RepositoryFactory private constructor(context: Context) {
    companion object {
        private const val SHARED_PREFERENCES_NAME = "PoolPredictor"
        private var instance: RepositoryFactory? = null

        fun getInstance(context: Context): RepositoryFactory {
            return instance ?: synchronized(this) {
                instance ?: RepositoryFactory(context).also { instance = it }
            }
        }
    }

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    private val configRepository: ConfigRepository by lazy {
        ConfigRepositoryImpl(sharedPreferences)
    }

    val aimTabRepository: AimTabRepository by lazy {
        AimTabRepository(configRepository)
    }

    val espTabRepository: EspTabRepository by lazy {
        EspTabRepository(configRepository)
    }

    val tablePositionRepository: TablePositionRepository by lazy {
        TablePositionRepository(configRepository)
    }

}
