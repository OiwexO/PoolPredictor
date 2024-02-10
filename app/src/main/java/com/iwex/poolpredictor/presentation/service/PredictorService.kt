package com.iwex.poolpredictor.presentation.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.WindowManager
import android.widget.Toast
import com.iwex.poolpredictor.di.factory.ViewModelFactory
import com.iwex.poolpredictor.domain.NativeBridge
import com.iwex.poolpredictor.domain.model.TablePosition
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.view.EspView
import com.iwex.poolpredictor.presentation.view.menu.FloatingMenu
import com.iwex.poolpredictor.presentation.view.menu.FloatingMenuTouchListener
import com.iwex.poolpredictor.presentation.view.menu.tabs.AimTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.EspTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.OtherTab
import com.iwex.poolpredictor.presentation.view.tablePosition.OnTablePositionSavedListener
import com.iwex.poolpredictor.presentation.view.tablePosition.TablePositionEspView
import com.iwex.poolpredictor.presentation.view.tablePosition.TablePositionView
import com.iwex.poolpredictor.presentation.viewmodel.AimTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.EspTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.TablePositionViewModel

class PredictorService : Service() {
    private val windowManager: WindowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private var floatingMenu: FloatingMenu? = null
    //TODO Remove lateinit
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
        windowManager.addView(espView, espView.layoutParams)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFloatingMenu(aimTabViewModel: AimTabViewModel, espTabViewModel: EspTabViewModel) {
        val aimTab = AimTab(this, aimTabViewModel)
        val espTab = EspTab(this, espTabViewModel)
        val otherTab = OtherTab(this)
        val floatingMenu = FloatingMenu(this, aimTab, espTab, otherTab)
        floatingMenu.setOnTouchListener(
            FloatingMenuTouchListener(
                floatingMenu.layoutParams,
                Dimensions.getInstance(this).scaledTouchSlop
            ) { view, layoutParams -> windowManager.updateViewLayout(view, layoutParams) }
        )
        windowManager.addView(floatingMenu, floatingMenu.layoutParams)
        this.floatingMenu = floatingMenu
    }

    private fun setupNativeBridge(tablePosition: TablePosition) {
        // getPocketPositionsInScreen() should be called before
        // setEspView() to avoid wrong results in EspView.onDraw()
        espView.getPocketPositionsInScreen(tablePosition)
        NativeBridge.setEspView(espView)
    }

    //TODO Refactor this
    private fun startTablePositionSetup(tablePositionViewModel: TablePositionViewModel) {
        tablePositionView = TablePositionView(this, tablePositionViewModel)
        tablePositionView!!.onTablePositionSavedListener = object : OnTablePositionSavedListener {
            override fun onTablePositionSaved() {
                removeTablePositionSetup()
                startPredictor()
            }
        }
        tablePositionEspView = TablePositionEspView(this, tablePositionViewModel)
        windowManager.addView(tablePositionEspView, tablePositionEspView!!.layoutParams)
        windowManager.addView(tablePositionView, tablePositionView!!.layoutParams)
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

    companion object {
        private const val TOAST_TEXT = "YouTube: @iwex\nEnjoy!"
    }

}
