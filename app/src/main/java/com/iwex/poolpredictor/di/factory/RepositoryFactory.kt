package com.iwex.poolpredictor.di.factory

import android.content.Context
import android.content.SharedPreferences
import com.iwex.poolpredictor.data.local.repository.MenuStateRepositoryImpl
import com.iwex.poolpredictor.data.local.repository.TablePositionRepositoryImpl
import com.iwex.poolpredictor.domain.repository.MenuStateRepository
import com.iwex.poolpredictor.domain.repository.TablePositionRepository

class RepositoryFactory private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    val menuStateRepository: MenuStateRepository by lazy {
        MenuStateRepositoryImpl(sharedPreferences)
    }

    val tablePositionRepository: TablePositionRepository by lazy {
        TablePositionRepositoryImpl(sharedPreferences)
    }

    companion object {

        private const val SHARED_PREFERENCES_NAME = "PoolPredictor"
        private var instance: RepositoryFactory? = null

        fun getInstance(context: Context): RepositoryFactory {
            return instance ?: synchronized(this) {
                instance ?: RepositoryFactory(context).also { instance = it }
            }
        }
    }

}
