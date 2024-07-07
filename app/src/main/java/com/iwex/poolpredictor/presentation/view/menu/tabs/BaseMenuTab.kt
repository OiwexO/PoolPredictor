package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.content.Context
import android.widget.LinearLayout
import androidx.core.view.setPadding
import com.iwex.poolpredictor.presentation.resource.MenuColors
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.viewmodel.BaseMenuTabViewModel

abstract class BaseMenuTab(
    context: Context,
    private val viewModel: BaseMenuTabViewModel
) : LinearLayout(context) {
    init {
        initializeLayout()
    }

    private fun initializeLayout() {
        orientation = VERTICAL
        setBackgroundColor(MenuColors.TAB_BACKGROUND)
        setPadding(Dimensions.getInstance(context).tabPaddingPx)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.saveSettings()
    }
}
