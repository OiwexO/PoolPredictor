package com.iwex.poolpredictor.presentation.util

import android.os.Build
import android.view.WindowManager

class OverlayUtils {

    companion object {

        val overlayWindowType: Int
            get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_APPLICATION
    }
}