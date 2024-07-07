package com.iwex.poolpredictor.presentation.util

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.O
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

class OverlayUtils {
    companion object {
        val overlayWindowType: Int
            get() = if (SDK_INT >= O) TYPE_APPLICATION_OVERLAY else TYPE_APPLICATION
    }
}
