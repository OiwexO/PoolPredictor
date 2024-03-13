package com.iwex.poolpredictor.presentation

import android.annotation.SuppressLint
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
import com.iwex.poolpredictor.presentation.resource.MenuColors
import com.iwex.poolpredictor.presentation.resource.Dimensions

class MenuWidgetFactory {
    companion object {

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        fun addSwitch(
            value: Boolean,
            checkedListener: (isChecked: Boolean) -> Unit,
            label: String,
            context: Context,
            parent: ViewGroup
        ): Switch {
            return Switch(context).apply {
                isChecked = value
                text = label
                textSize = Dimensions.getInstance(context).switchTextSizeSp
                minHeight = Dimensions.getInstance(context).switchHeightPx
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
            val textViewLabel = addSeekBarLabel(label, progress, context, parent)
            return SeekBar(context).apply {
                this.max = max
                this.progress = progress
                thumbTintList = ColorStateList.valueOf(MenuColors.SEEKBAR_THUMB)
                progressDrawable.colorFilter = PorterDuffColorFilter(
                    MenuColors.SEEKBAR_PROGRESS, PorterDuff.Mode.SRC_ATOP
                )
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    minHeight = Dimensions.getInstance(context).seekbarHeightPx
                }
                parent.addView(this)
                setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                        textViewLabel.text = String.format(label, progress)
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
            return TextView(context).apply {
                text = String.format(label, progress)
                textSize = Dimensions.getInstance(context).seekbarTextSizeSp
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
            return Button(context).apply {
                text = label
                textSize = Dimensions.getInstance(context).buttonTextSizeSp
                if (addMargin) {
                    layoutParams = LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                        setMargins(Dimensions.getInstance(context).buttonMarginPx)
                    }
                }
                background = getButtonBackground(context)
                setTextColor(MenuColors.BUTTON_TEXT)
                gravity = Gravity.CENTER
                parent.addView(this)
            }
        }

        private fun getButtonBackground(context: Context): GradientDrawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = Dimensions.getInstance(context).buttonCornerRadiusPx
                setColor(MenuColors.BUTTON_BACKGROUND)
            }
        }

        fun addTitle(
            label: String,
            context: Context,
            parent: ViewGroup
        ): TextView {
            return TextView(context).apply {
                text = label
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                setTextColor(MenuColors.MAIN_TEXT)
                textSize = Dimensions.getInstance(context).titleTextSizeSp
                parent.addView(this)
            }
        }

    }
}