package com.iwex.poolpredictor.app.repository

interface ConfigRepository {

    fun getInt(key: String, defValue: Int): Int

    fun putInt(key: String, value: Int)

    fun getBoolean(key: String, defValue: Boolean): Boolean

    fun putBoolean(key: String, value: Boolean)

}