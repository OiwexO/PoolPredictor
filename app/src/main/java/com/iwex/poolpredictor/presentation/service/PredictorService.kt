package com.iwex.poolpredictor.presentation.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.view.ViewConfiguration
import android.view.WindowManager
import android.widget.Toast
import com.iwex.poolpredictor.di.factory.UseCaseFactory
import com.iwex.poolpredictor.di.factory.ViewModelFactory
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.view.PredictionView
import com.iwex.poolpredictor.presentation.view.menu.FloatingMenu
import com.iwex.poolpredictor.presentation.view.menu.FloatingMenuTouchListener
import com.iwex.poolpredictor.presentation.view.menu.tabs.AimTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.EspTab
import com.iwex.poolpredictor.presentation.view.menu.tabs.OtherTab
import com.iwex.poolpredictor.presentation.view.tablePosition.OnTablePositionSetListener
import com.iwex.poolpredictor.presentation.view.tablePosition.TablePositionSetupView
import com.iwex.poolpredictor.presentation.view.tablePosition.TableShapeView
import com.iwex.poolpredictor.presentation.viewmodel.AimTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.OtherTabViewModel
import com.iwex.poolpredictor.presentation.viewmodel.esp.EspSharedViewModel

class PredictorService : Service() {
    private val windowManager: WindowManager by lazy {
        getSystemService(WINDOW_SERVICE) as WindowManager
    }

    private var floatingMenu: FloatingMenu? = null
    private var predictionView: PredictionView? = null
    private var tablePositionSetupView: TablePositionSetupView? = null
    private var tableShapeView: TableShapeView? = null

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_NOT_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        val isTableSet = UseCaseFactory.getInstance(this).getIsTableSetUseCase()
        if (isTableSet) {
            startPredictor()
        } else {
            startTablePositionSetup()
        }
    }

    private fun startTablePositionSetup() {
        val viewModelFactory = ViewModelFactory.getInstance(this)
        val tablePositionViewModel = viewModelFactory.tablePositionSharedViewModel
        val tablePositionSetupView = TablePositionSetupView(this, tablePositionViewModel)
        tablePositionSetupView.onTablePositionSetListener = object : OnTablePositionSetListener {
            override fun onTablePositionSet() {
                removeTablePositionSetup()
                startPredictor()
            }
        }
        val tableShapeView = TableShapeView(this, tablePositionViewModel)
        windowManager.addView(tableShapeView, tableShapeView.layoutParams)
        windowManager.addView(tablePositionSetupView, tablePositionSetupView.layoutParams)
        this.tablePositionSetupView = tablePositionSetupView
        this.tableShapeView = tableShapeView
    }

    private fun startPredictor() {
        Toast.makeText(this, Strings.TOAST_AUTHOR, Toast.LENGTH_LONG).show()
        val viewModelFactory = ViewModelFactory.getInstance(this)
        val espTabViewModel = viewModelFactory.espSharedViewModel
        val aimTabViewModel = viewModelFactory.aimTabViewModel
        val otherTabViewModel = viewModelFactory.otherTabViewModel
        setupPredictionView(espTabViewModel)
        setupFloatingMenu(aimTabViewModel, espTabViewModel, otherTabViewModel)
    }

    private fun setupPredictionView(espTabViewModel: EspSharedViewModel) {
        val predictionView = PredictionView(this, espTabViewModel)
        windowManager.addView(predictionView, predictionView.layoutParams)
        this.predictionView = predictionView
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupFloatingMenu(
        aimTabViewModel: AimTabViewModel,
        espSharedViewModel: EspSharedViewModel,
        otherTabViewModel: OtherTabViewModel
    ) {
        val aimTab = AimTab(this, aimTabViewModel)
        val espTab = EspTab(this, espSharedViewModel)
        val otherTab = OtherTab(this, otherTabViewModel)
        val floatingMenu = FloatingMenu(this, aimTab, espTab, otherTab)
        floatingMenu.setOnTouchListener(
            FloatingMenuTouchListener(
                floatingMenu.layoutParams,
                ViewConfiguration.get(this).scaledTouchSlop.toFloat()
            ) { view, layoutParams -> windowManager.updateViewLayout(view, layoutParams) }
        )
        windowManager.addView(floatingMenu, floatingMenu.layoutParams)
        this.floatingMenu = floatingMenu
    }

    private fun removeTablePositionSetup() {
        tablePositionSetupView?.let {
            windowManager.removeView(it)
        }
        tablePositionSetupView = null
        tableShapeView?.let {
            windowManager.removeView(it)
        }
        tableShapeView = null
    }

    private fun removePredictor() {
        floatingMenu?.let {
            windowManager.removeView(it)
        }
        floatingMenu = null
        predictionView?.let {
            windowManager.removeView(it)
        }
        predictionView = null
    }

    override fun onDestroy() {
        super.onDestroy()
        removeTablePositionSetup()
        removePredictor()
    }
}