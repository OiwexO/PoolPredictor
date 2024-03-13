package com.iwex.poolpredictor.presentation.viewmodel.tablePosition

import androidx.lifecycle.LiveData
import com.iwex.poolpredictor.domain.model.TablePosition

interface TableShapeViewModel {

    val tablePosition: LiveData<TablePosition>
}