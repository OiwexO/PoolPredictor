package com.iwex.poolpredictor.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.iwex.poolpredictor.domain.usecase.table.GetIsTableSetUseCase

class LauncherViewModel(private val getIsTableSetUseCase: GetIsTableSetUseCase) : ViewModel() {
    private val _isTableSet = MutableLiveData<Boolean>()
    val isTableSet: LiveData<Boolean> get() = _isTableSet

    fun getIsTableSet() {
        _isTableSet.value = getIsTableSetUseCase()
    }
}
