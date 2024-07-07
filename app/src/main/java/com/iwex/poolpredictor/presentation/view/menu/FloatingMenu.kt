package com.iwex.poolpredictor.presentation.view.menu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PixelFormat
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.resource.FloatingIcon
import com.iwex.poolpredictor.presentation.resource.MenuColors
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.util.OverlayUtils
import com.iwex.poolpredictor.presentation.view.menu.tabs.AimTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.BaseMenuTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.EspTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.OtherTab

@SuppressLint("ViewConstructor")
class FloatingMenu(
    context: Context,
    private val aimTab: AimTab,
    private val espTab: EspTab,
    private val otherTab: OtherTab
) : RelativeLayout(context) {
    private val defaultTab = aimTab
    private val dimensions = Dimensions.getInstance(context)
    val layoutParams = initLayoutParams()
    private val floatingIconView = initFloatingIconView()
    private val tabHolderScrollView = initTabHolderScrollView()
    private val tabButtonsLayout = initTabButtonsLayout()
    private val floatingMenuLayout = initFloatingMenuLayout()
    private val closeMenuButton = initCloseMenuButton()

    init {
        closeMenuButton.setOnClickListener {
            setActiveTab(defaultTab)
            toggleIconAndMenuVisibility()
        }
        setActiveTab(defaultTab)
        addView(floatingIconView)
        addView(floatingMenuLayout)
    }

    private fun initLayoutParams() = WindowManager.LayoutParams(
        WRAP_CONTENT,
        WRAP_CONTENT,
        dimensions.iconPositionX,
        dimensions.iconPositionY,
        OverlayUtils.overlayWindowType,
        FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT
    )

    private fun initFloatingIconView() = ImageView(context).apply {
        layoutParams = LayoutParams(
            dimensions.iconSizePx,
            dimensions.iconSizePx
        )
        scaleType = ImageView.ScaleType.FIT_XY
        setImageBitmap(FloatingIcon.getBitmap())
    }

    private fun initTabHolderScrollView() = ScrollView(context).apply {
        layoutParams = LayoutParams(MATCH_PARENT, 0, 1f).apply {
            setMargins(dimensions.buttonMarginPx)
        }
    }

    private fun initTabButtonsLayout() = LinearLayout(context).apply {
        orientation = HORIZONTAL
        addTabButton(Strings.LABEL_AIM_BUTTON, aimTab, this)
        addTabButton(Strings.LABEL_ESP_BUTTON, espTab, this)
        addTabButton(Strings.LABEL_OTHER_BUTTON, otherTab, this)
    }

    private fun addTabButton(label: String, tab: BaseMenuTab, parent: ViewGroup) {
        MenuWidgetFactory.addButton(label, false, context, parent).apply {
            layoutParams = LayoutParams(0, WRAP_CONTENT, 1f).apply {
                setMargins(
                    dimensions.buttonMarginPx,
                    0,
                    dimensions.buttonMarginPx,
                    0
                )
            }
            setOnClickListener { setActiveTab(tab) }
        }
    }

    private fun setActiveTab(tab: BaseMenuTab) {
        tabHolderScrollView.removeAllViews()
        tabHolderScrollView.addView(tab)
    }

    private fun initFloatingMenuLayout() = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = LayoutParams(
            dimensions.menuWidthPx,
            dimensions.menuHeightPx
        )
        setBackgroundColor(MenuColors.MENU_BACKGROUND)
        MenuWidgetFactory.addTitle(Strings.LABEL_MENU_TITLE, context, this)
        visibility = GONE
        addView(tabHolderScrollView)
        addView(tabButtonsLayout)
    }

    private fun initCloseMenuButton() = MenuWidgetFactory.addButton(
        Strings.LABEL_CLOSE_MENU_BUTTON,
        true,
        context,
        floatingMenuLayout
    )

    private fun toggleIconAndMenuVisibility() {
        floatingIconView.isVisible = !floatingIconView.isVisible
        floatingMenuLayout.isVisible = !floatingMenuLayout.isVisible
    }

    override fun performClick(): Boolean {
        super.performClick()
        toggleIconAndMenuVisibility()
        return true
    }
}
