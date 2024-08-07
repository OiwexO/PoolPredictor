package com.iwex.poolpredictor.presentation.view

import android.content.Context
import android.graphics.PixelFormat
import android.view.View
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import com.iwex.poolpredictor.presentation.util.OverlayUtils

abstract class NonInteractiveOverlayView(context: Context) : View(context) {
    val layoutParams = LayoutParams(
        MATCH_PARENT,
        MATCH_PARENT,
        OverlayUtils.overlayWindowType,
        FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCHABLE,
        PixelFormat.TRANSLUCENT
    )
}
