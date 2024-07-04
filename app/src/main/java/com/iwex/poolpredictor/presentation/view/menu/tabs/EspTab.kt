package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.viewmodel.esp.EspTabViewModel

@SuppressLint("ViewConstructor")
class EspTab(context: Context, private val viewModel: EspTabViewModel) : BaseMenuTab(context) {

    private val lineWidthSeekbar: SeekBar
    private val ballRadiusSeekbar: SeekBar
    private val trajectoryOpacitySeekbar: SeekBar
    private val shotStateCircleWidthSeekbar: SeekBar
    private val shotStateCircleRadiusSeekbar: SeekBar
    private val shotStateCircleOpacitySeekbar: SeekBar
    private val resetTableButton: Button

    init {
        val espSettings = viewModel.getEspSettings()
        lineWidthSeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_LINE_WIDTH_SEEKBAR,
            MAX_LINE_WIDTH,
            espSettings.lineWidth,
            viewModel::onLineWidthChange,
            context,
            this
        )
        ballRadiusSeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_BALL_RADIUS_SEEKBAR,
            MAX_BALL_RADIUS,
            espSettings.ballRadius,
            viewModel::onBallRadiusChange,
            context,
            this
        )
        trajectoryOpacitySeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_TRAJECTORY_OPACITY_SEEKBAR,
            MAX_TRAJECTORY_OPACITY,
            espSettings.trajectoryOpacity,
            viewModel::onTrajectoryOpacityChange,
            context,
            this
        )
        shotStateCircleWidthSeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_SHOT_STATE_CIRCLE_WIDTH_SEEKBAR,
            MAX_SHOT_STATE_CIRCLE_WIDTH,
            espSettings.shotStateCircleWidth,
            viewModel::onShotStateCircleWidthChange,
            context,
            this
        )
        shotStateCircleRadiusSeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_SHOT_STATE_CIRCLE_RADIUS_SEEKBAR,
            MAX_SHOT_STATE_CIRCLE_RADIUS,
            espSettings.shotStateCircleRadius,
            viewModel::onShotStateCircleRadiusChange,
            context,
            this
        )
        shotStateCircleOpacitySeekbar = MenuWidgetFactory.addSeekBar(
            Strings.LABEL_SHOT_STATE_CIRCLE_OPACITY_SEEKBAR,
            MAX_SHOT_STATE_CIRCLE_OPACITY,
            espSettings.shotStateCircleOpacity,
            viewModel::onShotStateCircleOpacityChange,
            context,
            this
        )
        resetTableButton = MenuWidgetFactory.addButton(
            Strings.LABEL_RESET_TABLE_BUTTON,
            false,
            context,
            this
        )
        resetTableButton.setOnClickListener {
            Toast.makeText(context, Strings.TOAST_LONG_CLICK_TO_RESET, Toast.LENGTH_LONG).show()
        }
        resetTableButton.setOnLongClickListener {
            viewModel.onResetTableListener()
            Toast.makeText(context, Strings.TOAST_TABLE_WAS_RESET, Toast.LENGTH_LONG).show()
            true
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        viewModel.saveEspSettings()
    }

    companion object {

        private const val MAX_LINE_WIDTH = 40
        private const val MAX_BALL_RADIUS = 80
        private const val MAX_TRAJECTORY_OPACITY = 100
        private const val MAX_SHOT_STATE_CIRCLE_WIDTH = 40
        private const val MAX_SHOT_STATE_CIRCLE_RADIUS = 160
        private const val MAX_SHOT_STATE_CIRCLE_OPACITY = 100
    }
}