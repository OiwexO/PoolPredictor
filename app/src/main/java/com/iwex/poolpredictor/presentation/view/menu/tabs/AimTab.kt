package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.annotation.SuppressLint
import android.content.Context
import android.widget.SeekBar
import android.widget.Switch
import com.iwex.poolpredictor.domain.model.MAX_CUE_POWER
import com.iwex.poolpredictor.domain.model.MAX_CUE_SPIN
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.viewmodel.AimTabViewModel

@SuppressLint("UseSwitchCompatOrMaterialCode", "ViewConstructor")
class AimTab(
    context: Context,
    private val viewModel: AimTabViewModel
) : BaseMenuTab(context, viewModel) {
    private val drawLinesSwitch: Switch
    private val drawShotStateSwitch: Switch
    private val drawOpponentsLinesSwitch: Switch
    private val powerControlSwitch: Switch
    private val cuePowerSeekbar: SeekBar
    private val cueSpinSeekbar: SeekBar

    init {
        val aimSettings = viewModel.getAimSettings()
        drawLinesSwitch = MenuWidgetFactory.addSwitch(
            aimSettings.drawLinesEnabled,
            viewModel::onDrawLinesChange,
            Strings.LABEL_DRAW_LINES_SWITCH,
            context,
            this
        )
        drawShotStateSwitch = MenuWidgetFactory.addSwitch(
            aimSettings.drawShotStateEnabled,
            viewModel::onDrawShotStateChange,
            Strings.LABEL_DRAW_SHOT_STATE_SWITCH,
            context,
            this
        )
        drawOpponentsLinesSwitch = MenuWidgetFactory.addSwitch(
            aimSettings.drawOpponentsLinesEnabled,
            viewModel::onDrawOpponentsLinesChange,
            Strings.LABEL_DRAW_OPPONENTS_LINES_SWITCH,
            context,
            this
        )
        powerControlSwitch = MenuWidgetFactory.addSwitch(
            aimSettings.preciseTrajectoriesEnabled,
            viewModel::onPreciseTrajectoriesEnabledChange,
            Strings.LABEL_POWER_CONTROL_MODE_SWITCH,
            context,
            this
        )
        cuePowerSeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_CUE_POWER_SEEKBAR,
            MAX_CUE_POWER,
            aimSettings.cuePower,
            viewModel::onCuePowerChange,
            context,
            this
        )
        cueSpinSeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_CUE_SPIN_SEEKBAR,
            MAX_CUE_SPIN,
            aimSettings.cueSpin,
            viewModel::onCueSpinChange,
            context,
            this
        )
    }
}
