package com.iwex.poolpredictor.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout.LayoutParams
import android.widget.LinearLayout.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout.LayoutParams.WRAP_CONTENT
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.setMargins
import com.iwex.poolpredictor.presentation.resource.Dimensions
import com.iwex.poolpredictor.presentation.resource.MenuColors
import com.iwex.poolpredictor.presentation.util.StringUtils.Companion.formatStringWithNumber
import com.iwex.poolpredictor.presentation.view.ArrowButton

class MenuWidgetFactory {

    companion object {

        fun addSwitch(
            isChecked: Boolean,
            checkedListener: (isChecked: Boolean) -> Unit,
            label: String,
            context: Context,
            parent: ViewGroup
        ): Switch {
            val dimensions = Dimensions.getInstance(context)
            return Switch(context).apply {
                this.isChecked = isChecked
                text = label
                textSize = dimensions.switchTextSizeSp
                minHeight = dimensions.switchHeightPx
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    thumbTintList = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                        intArrayOf(MenuColors.SWITCH_THUMB_ENABLED, MenuColors.SWITCH_THUMB_DISABLED)
                    )
                    trackTintList = ColorStateList(
                        arrayOf(intArrayOf(android.R.attr.state_checked), intArrayOf()),
                        intArrayOf(MenuColors.SWITCH_TRACK_ENABLED, MenuColors.SWITCH_TRACK_DISABLED)
                    )
                }
                setTextColor(MenuColors.MAIN_TEXT)
                parent.addView(this)
                setOnCheckedChangeListener { _, isChecked ->
                    checkedListener(isChecked)
                }
            }
        }

        fun addSeekBar(
            label: String,
            max: Int,
            progress: Int,
            progressListener: (progress: Int) -> Unit,
            context: Context,
            parent: ViewGroup
        ): SeekBar {
            val dimensions = Dimensions.getInstance(context)
            val textViewLabel = addSeekBarLabel(label, progress, context, parent)
            return SeekBar(context).apply {
                this.max = max
                this.progress = progress
                thumbTintList = ColorStateList.valueOf(MenuColors.SEEKBAR_THUMB)
                progressDrawable.colorFilter = PorterDuffColorFilter(
                    MenuColors.SEEKBAR_PROGRESS, PorterDuff.Mode.SRC_ATOP
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    minHeight = dimensions.seekbarHeightPx
                }
                parent.addView(this)
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        textViewLabel.text = formatStringWithNumber(label, progress)
                        progressListener(progress)
                    }
                    override fun onStartTrackingTouch(seekBar: SeekBar) {}
                    override fun onStopTrackingTouch(seekBar: SeekBar) {}
                })
            }
        }

        private fun addSeekBarLabel(
            label: String,
            progress: Int,
            context: Context,
            parent: ViewGroup
        ): TextView {
            val dimensions = Dimensions.getInstance(context)
            return TextView(context).apply {
                text = formatStringWithNumber(label, progress)
                textSize = dimensions.seekbarTextSizeSp
                setTextColor(MenuColors.MAIN_TEXT)
                parent.addView(this)
            }
        }

        fun addButton(
            label: String,
            addMargin: Boolean,
            context: Context,
            parent: ViewGroup
        ): Button {
            val dimensions = Dimensions.getInstance(context)
            return Button(context).apply {
                text = label
                textSize = dimensions.buttonTextSizeSp
                if (addMargin) {
                    layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                        setMargins(dimensions.buttonMarginPx)
                    }
                }
                background = getButtonBackground(context)
                setTextColor(MenuColors.BUTTON_TEXT)
                gravity = Gravity.CENTER
                parent.addView(this)
            }
        }

        private fun getButtonBackground(context: Context): GradientDrawable {
            val dimensions = Dimensions.getInstance(context)
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = dimensions.buttonCornerRadiusPx
                setColor(MenuColors.BUTTON_BACKGROUND)
            }
        }

        fun addTitle(
            label: String,
            context: Context,
            parent: ViewGroup
        ): TextView {
            val dimensions = Dimensions.getInstance(context)
            return TextView(context).apply {
                text = label
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                setTextColor(MenuColors.MAIN_TEXT)
                textSize = dimensions.titleTextSizeSp
                parent.addView(this)
            }
        }

        fun addArrowButtonLeft(context: Context, parent: ViewGroup) : ArrowButton {
            return addArrowButton(context, parent, ArrowButton.ArrowDirection.LEFT)
        }

        fun addArrowButtonTop(context: Context, parent: ViewGroup) : ArrowButton {
            return addArrowButton(context, parent, ArrowButton.ArrowDirection.TOP)
        }

        fun addArrowButtonRight(context: Context, parent: ViewGroup) : ArrowButton {
            return addArrowButton(context, parent, ArrowButton.ArrowDirection.RIGHT)
        }

        fun addArrowButtonBottom(context: Context, parent: ViewGroup) : ArrowButton {
            return addArrowButton(context, parent, ArrowButton.ArrowDirection.BOTTOM)
        }

        private fun addArrowButton(
            context: Context,
            parent: ViewGroup,
            arrowDirection: ArrowButton.ArrowDirection
        ) : ArrowButton {
            val dimensions = Dimensions.getInstance(context)
            val size = dimensions.arrowButtonSizePx
            val cornerRadius = dimensions.buttonCornerRadiusPx
            return ArrowButton(context, size, cornerRadius, arrowDirection).apply {
                parent.addView(this)
            }
        }
    }
}