package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.ScrollView
import androidx.core.view.setMargins
import com.iwex.poolpredictor.app.NativeBridge
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.MenuWidgetFactory

@SuppressLint("ViewConstructor")
class FloatingMenuLayout(
    context: Context,
    private val aimTabLayout: AimTabLayout,
    private val espTabLayout: EspTabLayout
) : RelativeLayout(context) {

    private val floatingIconView: ImageView
    private val floatingMenuLayout: LinearLayout
    private val tabHolderScrollView: ScrollView
    private val tabButtonsLayout = LinearLayout(context)
    private val closeMenuButton: Button

    companion object {
        private const val TAG = "FloatingMenu.kt"
        private const val LABEL_MENU_TITLE = "Pool Predictor by IWEX"
        private const val LABEL_AIM_BUTTON = "AIM"
        private const val LABEL_ESP_BUTTON = "ESP"
        private const val LABEL_OTHER_BUTTON = "OTHER"
        private const val LABEL_CLOSE_MENU_BUTTON = "CLOSE MENU"

        private val DEFAULT_TAB = TabTypes.AIM
        enum class TabTypes {
            AIM,
            ESP,
            OTHER
        }
    }



    init {
        floatingIconView = initFloatingIconView(context)
        addView(floatingIconView)

        floatingMenuLayout = initFloatingMenuLayout(context)
        addView(floatingMenuLayout)

        tabHolderScrollView = initTabHolderScrollView(context)
        floatingMenuLayout.addView(tabHolderScrollView)
        setActiveTab(DEFAULT_TAB)
        floatingMenuLayout.addView(tabButtonsLayout)

        addTabButton(TabTypes.AIM, context)
        addTabButton(TabTypes.ESP, context)
        addTabButton(TabTypes.OTHER, context)

        closeMenuButton = MenuWidgetFactory.addButton(
            LABEL_CLOSE_MENU_BUTTON,
            true,
            context,
            floatingMenuLayout
        )
        closeMenuButton.setOnClickListener {
            setActiveTab(TabTypes.AIM)
            floatingMenuLayout.visibility = GONE
            floatingIconView.visibility = VISIBLE
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initFloatingIconView(context: Context): ImageView {
        val iconSize = MenuDesign.Measurements.ICON_SIZE
        val decoded: ByteArray = Base64.decode(NativeBridge.getIcon(), 0)

        return ImageView(context).apply {
            layoutParams = LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.height = iconSize
            layoutParams.width = iconSize
            scaleType = ImageView.ScaleType.FIT_XY
            setImageBitmap(BitmapFactory.decodeByteArray(decoded, 0, decoded.size))

        }
    }

    private fun initFloatingMenuLayout(context: Context): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LayoutParams(
                MenuDesign.Measurements.MENU_WIDTH,
                MenuDesign.Measurements.MENU_HEIGHT
            )
            setBackgroundColor(MenuDesign.Colors.MENU_BACKGROUND)
            MenuWidgetFactory.addTitle(LABEL_MENU_TITLE, context, this)
            visibility = GONE
        }
    }

    fun onIconClickListener() {
        floatingIconView.visibility = GONE
        floatingMenuLayout.visibility = VISIBLE
    }

    private fun initTabHolderScrollView(context: Context): ScrollView {
        return ScrollView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                0,
                1f
            ).apply {
                setMargins(MenuDesign.Measurements.BUTTON_MARGIN)
            }
        }
    }

    private fun setActiveTab(tab: TabTypes) {
        tabHolderScrollView.removeAllViews()
        when (tab) {
            TabTypes.AIM -> tabHolderScrollView.addView(aimTabLayout)
            TabTypes.ESP -> tabHolderScrollView.addView(espTabLayout)
            TabTypes.OTHER -> {}
        }

    }

    private fun addTabButton(tab: TabTypes, context: Context) {
        val label = when (tab) {
            TabTypes.AIM -> LABEL_AIM_BUTTON
            TabTypes.ESP -> LABEL_ESP_BUTTON
            TabTypes.OTHER -> LABEL_OTHER_BUTTON
        }
        val marginPx = MenuDesign.Measurements.BUTTON_MARGIN
        MenuWidgetFactory.addButton(label, false, context, tabButtonsLayout).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                setMargins(marginPx, 0, marginPx, 0)
            }
            setOnClickListener { setActiveTab(tab) }
        }
    }

}
