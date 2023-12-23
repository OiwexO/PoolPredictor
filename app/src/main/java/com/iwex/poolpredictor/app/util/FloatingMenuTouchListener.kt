package com.iwex.poolpredictor.app.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.WindowManager.LayoutParams
import com.iwex.poolpredictor.app.view.FloatingMenuLayout

class FloatingMenuTouchListener(
    private val windowManager: WindowManager,
    private val floatingMenu: FloatingMenuLayout,
    private val layoutParams: LayoutParams
) : View.OnTouchListener {
    private var initialTouchX = 0f
    private var initialTouchY = 0f
    private var initialX = 0
    private var initialY = 0

    private val minAlpha = 0.5f
    private val maxAlpha = 1.0f
    private val slopDistance = MenuDesign.slopDistance

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                floatingMenu.animate().alpha(minAlpha)
                initialX = layoutParams.x
                initialY = layoutParams.y
                initialTouchX = motionEvent.rawX
                initialTouchY = motionEvent.rawY
            }

            MotionEvent.ACTION_UP -> {
                floatingMenu.animate().alpha(maxAlpha)

                // Check if it's a swipe or click event
                val deltaX = motionEvent.rawX - initialTouchX
                val deltaY = motionEvent.rawY - initialTouchY
                if (deltaX < slopDistance && deltaY < slopDistance) {
                    floatingMenu.onIconClickListener()
                }
            }

            MotionEvent.ACTION_MOVE -> {
                val deltaX = motionEvent.rawX - initialTouchX
                val deltaY = motionEvent.rawY - initialTouchY

                layoutParams.apply {
                    x = (initialX + deltaX).toInt()
                    y = (initialY + deltaY).toInt()
                }
                windowManager.updateViewLayout(floatingMenu, layoutParams)
            }

            MotionEvent.ACTION_CANCEL -> {
                floatingMenu.animate().alpha(maxAlpha)
            }
        }
        return true
    }
}