package com.iwex.poolpredictor.presentation.view.menu

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.PixelFormat
import android.util.Base64
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.view.isVisible
import androidx.core.view.setMargins
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.resource.FloatingIcon
import com.iwex.poolpredictor.presentation.resource.MenuColors
import com.iwex.poolpredictor.presentation.view.menu.tabs.AimTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.BaseMenuTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.EspTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.OtherTab
import com.iwex.poolpredictor.presentation.util.OverlayUtils

@SuppressLint("ViewConstructor")
class FloatingMenu(
    context: Context,
    private val aimTab: AimTab,
    private val espTab: EspTab,
    private val otherTab: OtherTab
) : RelativeLayout(context) {

    val layoutParams: WindowManager.LayoutParams
    private val floatingIconView: ImageView
    private val floatingMenuLayout: LinearLayout
    private val tabHolderScrollView: ScrollView
    private val tabButtonsLayout: LinearLayout
    private val closeMenuButton: Button
    private val defaultTab = aimTab

    init {
        layoutParams = initLayoutParams(context)
        floatingIconView = initFloatingIconView(context)
        floatingMenuLayout = initFloatingMenuLayout(context)
        tabHolderScrollView = initTabHolderScrollView(context)
        tabButtonsLayout = initTabButtonsLayout(context)
        floatingMenuLayout.addView(tabHolderScrollView)
        floatingMenuLayout.addView(tabButtonsLayout)
        addTabButton(LABEL_AIM_BUTTON, aimTab, context)
        addTabButton(LABEL_ESP_BUTTON, espTab, context)
        addTabButton(LABEL_OTHER_BUTTON, otherTab, context)
        setActiveTab(defaultTab)
        closeMenuButton = initCloseMenuButton(context)
        closeMenuButton.setOnClickListener {
            setActiveTab(defaultTab)
            toggleIconAndMenuVisibility()
        }
        addView(floatingIconView)
        addView(floatingMenuLayout)
    }

    private fun toggleIconAndMenuVisibility() {
        if (floatingIconView.isVisible) {
            floatingIconView.visibility = GONE
            floatingMenuLayout.visibility = VISIBLE
        } else {
            floatingIconView.visibility = VISIBLE
            floatingMenuLayout.visibility = GONE
        }
    }

    private fun initLayoutParams(context: Context): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            WRAP_CONTENT,
            WRAP_CONTENT,
            Dimensions.getInstance(context).iconPositionX,
            Dimensions.getInstance(context).iconPositionY,
            OverlayUtils.overlayWindowType,
            FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun initFloatingIconView(context: Context): ImageView {
        val decoded: ByteArray = Base64.decode(FloatingIcon.BASE64_ENCODED_ICON, Base64.DEFAULT)
        return ImageView(context).apply {
            layoutParams = LayoutParams(
                Dimensions.getInstance(context).iconSizePx,
                Dimensions.getInstance(context).iconSizePx
            )
            scaleType = ImageView.ScaleType.FIT_XY
            setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.size))
        }
    }

    private fun initFloatingMenuLayout(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                Dimensions.getInstance(context).menuWidthPx,
                Dimensions.getInstance(context).menuHeightPx
            )
            setBackgroundColor(MenuColors.MENU_BACKGROUND)
            MenuWidgetFactory.addTitle(LABEL_MENU_TITLE, context, this)
            visibility = GONE
        }
    }

    private fun initTabHolderScrollView(context: Context): ScrollView {
        return ScrollView(context).apply {
            layoutParams = LayoutParams(MATCH_PARENT, 0, 1f).apply {
                setMargins(Dimensions.getInstance(context).buttonMarginPx)
            }
        }
    }

    private fun initTabButtonsLayout(context: Context): LinearLayout {
        return LinearLayout(context)
    }

    private fun initCloseMenuButton(context: Context): Button {
        return MenuWidgetFactory.addButton(
            LABEL_CLOSE_MENU_BUTTON,
            true,
            context,
            floatingMenuLayout
        )
    }

    private fun setActiveTab(tab: BaseMenuTab) {
        tabHolderScrollView.removeAllViews()
        tabHolderScrollView.addView(tab)
    }

    private fun addTabButton(label: String, tab: BaseMenuTab, context: Context) {
        MenuWidgetFactory.addButton(label, false, context, tabButtonsLayout).apply {
            layoutParams = LayoutParams(0, WRAP_CONTENT, 1f).apply {
                setMargins(
                    Dimensions.getInstance(context).buttonMarginPx,
                    0,
                    Dimensions.getInstance(context).buttonMarginPx,
                    0
                )
            }
            setOnClickListener { setActiveTab(tab) }
        }
    }

    override fun performClick(): Boolean {
        super.performClick()
        toggleIconAndMenuVisibility()
        return true
    }

    companion object {

        @Suppress("unused")
        private const val TAG = "FloatingMenu.kt"

        private const val LABEL_MENU_TITLE = "Pool Predictor by OiwexO"
        private const val LABEL_AIM_BUTTON = "AIM"
        private const val LABEL_ESP_BUTTON = "ESP"
        private const val LABEL_OTHER_BUTTON = "OTHER"
        private const val LABEL_CLOSE_MENU_BUTTON = "CLOSE MENU"
    }
}
