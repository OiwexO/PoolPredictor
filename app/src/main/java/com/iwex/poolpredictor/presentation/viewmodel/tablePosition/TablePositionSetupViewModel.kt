package com.iwex.poolpredictor.presentation.viewmodel.tablePosition

import androidx.lifecycle.LiveData

interface TablePositionSetupViewModel {

    val isTableSet: LiveData<Boolean>

    val currentPointIndex: LiveData<Int>

    fun onButtonLeft()

    fun onButtonTop()

    fun onButtonRight()

    fun onButtonBottom()

    fun onSaveButton()

    fun onResetButton()

    fun onTableSet()
}