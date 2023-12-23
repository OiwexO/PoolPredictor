package com.iwex.poolpredictor.app.util

import android.graphics.PixelFormat
import android.os.Build
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams

class LayoutParamsProvider private constructor() {
    companion object {

        val espLayoutParams: LayoutParams
            get() = getOverlayLayoutParams()

        val floatingMenuLayoutParams: LayoutParams
            get() = getMenuLayoutParams(
                MenuDesign.Measurements.MENU_POS_X,
                MenuDesign.Measurements.MENU_POS_Y
            )

        val tablePositionLayoutParams: LayoutParams
            get() = getMenuLayoutParams()

        private fun getOverlayLayoutParams(): LayoutParams {
            val windowType = if (Build.VERSION.SDK_INT >= 26)
                    LayoutParams.TYPE_APPLICATION_OVERLAY
                else
                    LayoutParams.TYPE_APPLICATION
            return LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT,
                windowType,
                LayoutParams.FLAG_NOT_FOCUSABLE or LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT
            )
        }

        private fun getMenuLayoutParams(x: Int = 0, y: Int = 0): LayoutParams {
            val windowType =
                if (Build.VERSION.SDK_INT >= 26) LayoutParams.TYPE_APPLICATION_OVERLAY
                else LayoutParams.TYPE_APPLICATION
            return LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                x,
                y,
                windowType,
                LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT
            )
        }

    }
}