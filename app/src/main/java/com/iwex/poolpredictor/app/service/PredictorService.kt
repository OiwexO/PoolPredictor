package com.iwex.poolpredictor.app.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import android.widget.Toast
import com.iwex.poolpredictor.app.NativeBridge
import com.iwex.poolpredictor.app.model.TablePosition
import com.iwex.poolpredictor.app.util.FloatingMenuTouchListener
import com.iwex.poolpredictor.app.util.LayoutParamsProvider
import com.iwex.poolpredictor.app.util.interfaces.OnButtonClickListener
import com.iwex.poolpredictor.app.view.AimTabLayout
import com.iwex.poolpredictor.app.view.EspTabLayout
import com.iwex.poolpredictor.app.view.EspView
import com.iwex.poolpredictor.app.view.FloatingMenuLayout
import com.iwex.poolpredictor.app.view.OtherTabLayout
import com.iwex.poolpredictor.app.view.TablePositionEspView
import com.iwex.poolpredictor.app.view.TablePositionView
import com.iwex.poolpredictor.app.viewmodel.AimTabViewModel
import com.iwex.poolpredictor.app.viewmodel.EspTabViewModel
import com.iwex.poolpredictor.app.viewmodel.TablePositionViewModel
import com.iwex.poolpredictor.app.viewmodel.ViewModelFactory

class PredictorService : Service(), OnButtonClickListener {
    private lateinit var windowManager: WindowManager
    private lateinit var floatingMenu: FloatingMenuLayout
    private lateinit var espView: EspView
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
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        val viewModelFactory = ViewModelFactory.getInstance(this)
        val tablePositionViewModel = viewModelFactory.tablePositionViewModel
        Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show()
        if (tablePositionViewModel.isTableSet) {
            startPredictor()
        } else {
            startTablePositionSetup(tablePositionViewModel)
        }
    }

    private fun startPredictor() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        val espTabViewModel = viewModelFactory.espTabViewModel
        val aimTabViewModel = viewModelFactory.aimTabViewModel
        val tablePosition = viewModelFactory.tablePositionViewModel.tablePosition
        setupEspView(espTabViewModel)
        setupFloatingMenu(aimTabViewModel, espTabViewModel)
        setupNativeBridge(tablePosition)
    }

    private fun setupEspView(espTabViewModel: EspTabViewModel) {
        espView = EspView(this, espTabViewModel)
        windowManager.addView(espView, LayoutParamsProvider.espLayoutParams)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFloatingMenu(aimTabViewModel: AimTabViewModel, espTabViewModel: EspTabViewModel) {
        val aimTabLayout = AimTabLayout(this, aimTabViewModel)
        val espTabLayout = EspTabLayout(this, espTabViewModel)
        val otherTabLayout = OtherTabLayout(this)
        floatingMenu = FloatingMenuLayout(this, aimTabLayout, espTabLayout, otherTabLayout)
        val floatingMenuLayoutParams = LayoutParamsProvider.floatingMenuLayoutParams
        floatingMenu.setOnTouchListener(FloatingMenuTouchListener(
            windowManager,
            floatingMenu,
            floatingMenuLayoutParams
        ))
        windowManager.addView(floatingMenu, floatingMenuLayoutParams)
    }

    private fun setupNativeBridge(tablePosition: TablePosition) {
        // getPocketPositionsInScreen() should be called before
        // setEspView() to avoid wrong results in EspView.onDraw()
        espView.getPocketPositionsInScreen(tablePosition)
        NativeBridge.setEspView(espView)
    }

    private fun startTablePositionSetup(tablePositionViewModel: TablePositionViewModel) {
        tablePositionViewModel.setOnButtonClickListener(this)
        tablePositionView = TablePositionView(this, tablePositionViewModel)
        tablePositionEspView = TablePositionEspView(this, tablePositionViewModel)
        windowManager.addView(tablePositionEspView, LayoutParamsProvider.espLayoutParams)
        windowManager.addView(tablePositionView, LayoutParamsProvider.tablePositionLayoutParams)
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

    private fun removePredictor() {
        windowManager.removeView(floatingMenu)
        windowManager.removeView(espView)
    }

    override fun onDestroy() {
        super.onDestroy()
        removeTablePositionSetup()
        removePredictor()
    }

    override fun onButtonClicked() {
        removeTablePositionSetup()
        startPredictor()
    }

    companion object {
        private const val TOAST_TEXT = "YouTube: @iwex\nEnjoy!"
    }

}
