package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.iwex.poolpredictor.presentation.resource.Colors
import com.iwex.poolpredictor.presentation.resource.Dimensions

open class BaseMenuTab(context: Context) : LinearLayout(context) {

    init {
        orientation = VERTICAL
        initializeLayout(context)
    }

    private fun initializeLayout(context: Context) {
        setBackgroundColor(Colors.TAB_BACKGROUND)
        setPadding(Dimensions.getInstance(context).tabPaddingPx)
    }

}