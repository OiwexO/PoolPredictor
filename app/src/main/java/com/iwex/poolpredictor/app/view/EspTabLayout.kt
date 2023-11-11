package com.iwex.poolpredictor.app.view

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.view.setPadding
import com.iwex.poolpredictor.app.util.MenuDesign
import com.iwex.poolpredictor.app.util.MenuWidgetFactory
import com.iwex.poolpredictor.app.viewmodel.EspTabViewModel

@SuppressLint("ViewConstructor")
class EspTabLayout(
    context: Context, private val viewModel: EspTabViewModel
) : LinearLayout(context) {

    private val lineWidthSeekbar: SeekBar
    private val ballRadiusSeekbar: SeekBar
    private val trajectoryOpacitySeekbar: SeekBar
    private val shotStateCircleWidthSeekbar: SeekBar
    private val shotStateCircleRadiusSeekbar: SeekBar
    private val shotStateCircleOpacitySeekbar: SeekBar
    private val resetTableButton: Button

    companion object {
        private const val LABEL_LINE_WIDTH_SEEKBAR = "line width: %d"
        private const val LABEL_BALL_RADIUS_SEEKBAR = "ball radius: %d"
        private const val LABEL_TRAJECTORY_OPACITY_SEEKBAR = "trajectory opacity: %d"
        private const val LABEL_SHOT_STATE_CIRCLE_WIDTH_SEEKBAR = "shot state circle width: %d"
        private const val LABEL_SHOT_STATE_CIRCLE_RADIUS_SEEKBAR = "shot state circle radius: %d"
        private const val LABEL_SHOT_STATE_CIRCLE_OPACITY_SEEKBAR = "shot state circle opacity: %d"
        private const val LABEL_RESET_TABLE_BUTTON = "reset table position\n(restart is required)"

        private const val TOAST_LONG_CLICK_TO_RESET = "hold button to reset table position"
        private const val TOAST_TABLE_WAS_RESET = "table has been reset, restart the app"

        private const val MAX_LINE_WIDTH = 40
        private const val MAX_BALL_RADIUS = 80
        private const val MAX_TRAJECTORY_OPACITY = 100
        private const val MAX_SHOT_STATE_CIRCLE_WIDTH = 40
        private const val MAX_SHOT_STATE_CIRCLE_RADIUS = 160
        private const val MAX_SHOT_STATE_CIRCLE_OPACITY = 100
    }

    init {
        orientation = VERTICAL
        setBackgroundColor(MenuDesign.Colors.TAB_BACKGROUND)
        setPadding(MenuDesign.Measurements.TAB_PADDING)

        lineWidthSeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_LINE_WIDTH_SEEKBAR,
            MAX_LINE_WIDTH,
            viewModel.getLineWidth(),
            viewModel::onLineWidthChange,
            context,
            this
        )

        ballRadiusSeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_BALL_RADIUS_SEEKBAR,
            MAX_BALL_RADIUS,
            viewModel.getBallRadius(),
            viewModel::onBallRadiusChange,
            context,
            this
        )

        trajectoryOpacitySeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_TRAJECTORY_OPACITY_SEEKBAR,
            MAX_TRAJECTORY_OPACITY,
            viewModel.getTrajectoryOpacity(),
            viewModel::onTrajectoryOpacityChange,
            context,
            this
        )

        shotStateCircleWidthSeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_SHOT_STATE_CIRCLE_WIDTH_SEEKBAR,
            MAX_SHOT_STATE_CIRCLE_WIDTH,
            viewModel.getShotStateCircleWidth(),
            viewModel::onShotStateCircleWidthChange,
            context,
            this
        )

        shotStateCircleRadiusSeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_SHOT_STATE_CIRCLE_RADIUS_SEEKBAR,
            MAX_SHOT_STATE_CIRCLE_RADIUS,
            viewModel.getShotStateCircleRadius(),
            viewModel::onShotStateCircleRadiusChange,
            context,
            this
        )

        shotStateCircleOpacitySeekbar = MenuWidgetFactory.addSeekBar(
            LABEL_SHOT_STATE_CIRCLE_OPACITY_SEEKBAR,
            MAX_SHOT_STATE_CIRCLE_OPACITY,
            viewModel.getShotStateCircleOpacity(),
            viewModel::onShotStateCircleOpacityChange,
            context,
            this
        )

        resetTableButton = MenuWidgetFactory.addButton(
            LABEL_RESET_TABLE_BUTTON,
            false,
            context,
            this
        )
        resetTableButton.setOnClickListener {
            Toast.makeText(context, TOAST_LONG_CLICK_TO_RESET, Toast.LENGTH_LONG).show()
        }
        resetTableButton.setOnLongClickListener {
            viewModel.onResetTableListener()
            Toast.makeText(context, TOAST_TABLE_WAS_RESET, Toast.LENGTH_LONG).show()
            true
        }

    }

}