package com.iwex.poolpredictor.di.factory

import android.content.Context
import android.content.SharedPreferences
import com.iwex.poolpredictor.data.repository.MenuSettingsRepositoryImpl
import com.iwex.poolpredictor.data.repository.NativeRepositoryImpl
import com.iwex.poolpredictor.data.repository.TablePositionRepositoryImpl
import com.iwex.poolpredictor.domain.repository.MenuSettingsRepository
import com.iwex.poolpredictor.domain.repository.NativeRepository
import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class RepositoryFactory private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    val menuSettingsRepository: MenuSettingsRepository by lazy {
        MenuSettingsRepositoryImpl(sharedPreferences)
    }

    val nativeRepository: NativeRepository by lazy {
        NativeRepositoryImpl()
    }

    val tablePositionRepository: TablePositionRepository by lazy {
        TablePositionRepositoryImpl(sharedPreferences)
    }

    companion object {
        private const val SHARED_PREFERENCES_NAME = "PoolPredictor"
        private var instance: RepositoryFactory? = null

        fun getInstance(context: Context) = instance ?: synchronized(this) {
            instance ?: RepositoryFactory(context).also { instance = it }
        }
    }
}
