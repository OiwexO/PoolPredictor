package com.iwex.poolpredictor.presentation.view

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.View
import android.view.WindowManager.LayoutParams
import android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
import android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION
import android.view.WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY

abstract class NonInteractiveOverlayView(context: Context) : View(context) {

    val layoutParams: LayoutParams

    init {
        val windowType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                TYPE_APPLICATION_OVERLAY
            else
                TYPE_APPLICATION
        layoutParams = LayoutParams(
            MATCH_PARENT,
            MATCH_PARENT,
            windowType,
            FLAG_NOT_FOCUSABLE or FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
    }
}