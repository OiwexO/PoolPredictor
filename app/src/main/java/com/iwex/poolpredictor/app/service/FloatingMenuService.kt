package com.iwex.poolpredictor.app.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.iwex.poolpredictor.app.NativeBridge
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.interfaces.OnButtonClickListener
import com.iwex.poolpredictor.app.view.AimTabLayout
import com.iwex.poolpredictor.app.view.EspTabLayout
import com.iwex.poolpredictor.app.view.EspView
import com.iwex.poolpredictor.app.view.FloatingMenuLayout
import com.iwex.poolpredictor.app.view.TablePositionEspView
import com.iwex.poolpredictor.app.view.TablePositionView
import com.iwex.poolpredictor.app.viewmodel.TablePositionViewModel
import com.iwex.poolpredictor.app.viewmodel.ViewModelFactory

class FloatingMenuService : Service(), OnButtonClickListener {
    private lateinit var windowManager: WindowManager

    private lateinit var floatingMenu: FloatingMenuLayout
    private lateinit var floatingMenuLayoutParams: WindowManager.LayoutParams
    private lateinit var espView: EspView

    private lateinit var tablePositionViewModel: TablePositionViewModel
    private var tablePositionView: TablePositionView? = null
    private var tablePositionEspView: TablePositionEspView? = null

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        MenuDesign.initDisplayMetrics(this)
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager

        val viewModelFactory = ViewModelFactory.getInstance(this)
        tablePositionViewModel = viewModelFactory.tablePositionViewModel

        if (tablePositionViewModel.isTableSet) {
            startFloatingMenu(this)
        } else {
            startTablePositionSetupMenu(this)
        }

    }

    private fun startFloatingMenu(context: Context) {
        setupEspView(context)
        setupFloatingMenu(context)
        setupNativeBridge()
    }

    private fun setupNativeBridge() {
        val tablePosition = tablePositionViewModel.tablePosition
        // getPocketPositionsInScreen() should be called before
        // setEspView() to avoid wrong results in EspView.onDraw()
        espView.getPocketPositionsInScreen(tablePosition)

        // setServiceContext() should be called before setEspView(),
        // otherwise the native predictor_thread will return
        // Check isToastShown usage in cpp/bridge/NativeBridge.cpp
        NativeBridge.setServiceContext(this@FloatingMenuService)
        NativeBridge.setEspView(espView)
    }

    private fun setupEspView(context: Context) {
        val viewModelFactory = ViewModelFactory.getInstance(context)
        val espViewModel = viewModelFactory.espTabViewModel
        espView = EspView(context, espViewModel)
        windowManager.addView(espView, getEspLayoutParams())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFloatingMenu(context: Context) {
        val viewModelFactory = ViewModelFactory.getInstance(context)
        val aimTabViewModel = viewModelFactory.aimTabViewModel
        val espTabViewModel = viewModelFactory.espTabViewModel

        val aimTabLayout = AimTabLayout(context, aimTabViewModel)
        val espTabLayout = EspTabLayout(context, espTabViewModel)
        floatingMenu = FloatingMenuLayout(context, aimTabLayout, espTabLayout)
        floatingMenu.setOnTouchListener(FloatingMenuTouchListener())

        floatingMenuLayoutParams = getMenuLayoutParams(
            MenuDesign.Measurements.MENU_POS_X, MenuDesign.Measurements.MENU_POS_Y
        )
        windowManager.addView(floatingMenu, floatingMenuLayoutParams)
    }

    private fun startTablePositionSetupMenu(context: Context) {
        tablePositionViewModel.onButtonClickListener = this
        tablePositionView = TablePositionView(context, tablePositionViewModel)
        tablePositionEspView = TablePositionEspView(context, tablePositionViewModel)

        windowManager.addView(tablePositionEspView, getEspLayoutParams())
        windowManager.addView(tablePositionView, getMenuLayoutParams())
    }

    private fun getEspLayoutParams(): WindowManager.LayoutParams {
        val windowType =
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_APPLICATION
//        val width = 2168
//        val height = 1080
        return WindowManager.LayoutParams(
//            width, height,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun getMenuLayoutParams(x: Int = 0, y: Int = 0): WindowManager.LayoutParams {
        val windowType =
            if (Build.VERSION.SDK_INT >= 26) WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else WindowManager.LayoutParams.TYPE_APPLICATION
        return WindowManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            x,
            y,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
    }

    private fun removeTablePositionSetup() {
        tablePositionView?.let {
            windowManager.removeView(it)
        }
        tablePositionView = null

        tablePositionEspView?.let {
            windowManager.removeView(it)
        }
        tablePositionEspView = null
    }

    private fun removeFloatingMenu() {
        windowManager.removeView(floatingMenu)
        windowManager.removeView(espView)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeTablePositionSetup()
        removeFloatingMenu()
    }

    override fun onButtonClicked() {
        removeTablePositionSetup()
        startFloatingMenu(this)
    }

    private inner class FloatingMenuTouchListener : View.OnTouchListener {
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
                    initialX = floatingMenuLayoutParams.x
                    initialY = floatingMenuLayoutParams.y
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

                    floatingMenuLayoutParams.apply {
                        x = (initialX + deltaX).toInt()
                        y = (initialY + deltaY).toInt()
                    }
                    windowManager.updateViewLayout(floatingMenu, floatingMenuLayoutParams)
                }

                MotionEvent.ACTION_CANCEL -> {
                    floatingMenu.animate().alpha(maxAlpha)
                }
            }
            return true
        }
    }

}
