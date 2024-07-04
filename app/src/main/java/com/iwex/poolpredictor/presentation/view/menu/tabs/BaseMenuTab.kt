package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.iwex.poolpredictor.presentation.resource.MenuColors
import com.iwex.poolpredictor.presentation.resource.Dimensions

abstract class BaseMenuTab(context: Context) : LinearLayout(context) {

    init {
        initializeLayout(context)
    }

    private fun initializeLayout(context: Context) {
        orientation = VERTICAL
        setBackgroundColor(MenuColors.TAB_BACKGROUND)
        setPadding(Dimensions.getInstance(context).tabPaddingPx)
    }

}