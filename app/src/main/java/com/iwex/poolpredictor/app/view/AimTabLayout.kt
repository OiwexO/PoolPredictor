package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Switch
import androidx.core.view.setPadding
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.MenuWidgetFactory
import com.iwex.poolpredictor.app.viewmodel.AimTabViewModel

@SuppressLint("UseSwitchCompatOrMaterialCode", "ViewConstructor")
class AimTabLayout(
    context: Context, private val viewModel: AimTabViewModel
) : LinearLayout(context) {
    private val drawLinesSwitch: Switch
    private val drawShotStateSwitch: Switch
    private val drawOpponentsLinesSwitch: Switch
    private val powerControlSwitch: Switch

    private val cuePowerSeekbar: SeekBar
    private val cueSpinSeekbar: SeekBar

    companion object {
        private const val LABEL_DRAW_LINES_SWITCH = "draw lines"
        private const val LABEL_DRAW_SHOT_STATE_SWITCH = "draw shot shotState"
        private const val LABEL_DRAW_OPPONENTS_LINES_SWITCH = "draw opponent\'s lines"
        private const val LABEL_POWER_CONTROL_MODE_SWITCH = "power control mode"
        private const val LABEL_CUE_POWER_SEEKBAR = "cue power: %d"
        private const val LABEL_CUE_SPIN_SEEKBAR = "cue spin: %d"

        private const val MAX_CUE_POWER = 13
        private const val MAX_CUE_SPIN = 13

    }

    init {
        orientation = VERTICAL
        setBackgroundColor(MenuDesign.Colors.TAB_BACKGROUND)
        setPadding(MenuDesign.Measurements.TAB_PADDING)
        val state = viewModel.getAimTabState()

        drawLinesSwitch = MenuWidgetFactory.addSwitch(
            state.drawLinesEnabled,
            viewModel::onDrawLinesChange,
            LABEL_DRAW_LINES_SWITCH,
            context,
            this
        )

        drawShotStateSwitch = MenuWidgetFactory.addSwitch(
            state.drawShotStateEnabled,
            viewModel::onDrawShotStateChange,
            LABEL_DRAW_SHOT_STATE_SWITCH,
            context,
            this
        )

        drawOpponentsLinesSwitch = MenuWidgetFactory.addSwitch(
            state.drawOpponentsLinesEnabled,
            viewModel::onDrawOpponentsLinesChange,
            LABEL_DRAW_OPPONENTS_LINES_SWITCH,
            context,
            this
        )

        powerControlSwitch = MenuWidgetFactory.addSwitch(
            state.powerControlModeEnabled,
            viewModel::onPowerControlModeEnabledChange,
            LABEL_POWER_CONTROL_MODE_SWITCH,
            context,
            this
        )

        cuePowerSeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_CUE_POWER_SEEKBAR,
            MAX_CUE_POWER,
            state.cuePower,
            viewModel::onCuePowerChange,
            context,
            this
        )

        cueSpinSeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_CUE_SPIN_SEEKBAR,
            MAX_CUE_SPIN,
            state.cueSpin,
            viewModel::onCueSpinChange,
            context,
            this
        )

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.saveState()
    }

}
