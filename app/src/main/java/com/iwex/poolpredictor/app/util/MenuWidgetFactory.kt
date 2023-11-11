package com.iwex.poolpredictor.app.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.core.view.setMargins

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
                minHeight = MenuDesign.Measurements.SWITCH_HEIGHT
//                if (Build.VERSION.SDK_INT >= 23) {
//                    thumbTintList = ColorStateList.valueOf(MenuDesign.Colors.SWITCH_THUMB)
//                    trackTintList = ColorStateList.valueOf(MenuDesign.Colors.BUTTON_TEXT)
//                }
                setTextColor(MenuDesign.Colors.MAIN)
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
                thumb = ColorDrawable(MenuDesign.Colors.MAIN)
                progressDrawable.colorFilter = PorterDuffColorFilter(
                    MenuDesign.Colors.SEEKBAR_PROGRESS, PorterDuff.Mode.SRC_ATOP
                )
                if (Build.VERSION.SDK_INT >= 29) {
                    minHeight = MenuDesign.Measurements.SEEKBAR_HEIGHT
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
                setTextColor(MenuDesign.Colors.MAIN)
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
                if (addMargin) {
                    layoutParams = LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(MenuDesign.Measurements.BUTTON_MARGIN)
                    }
                }
                background = getButtonBackground()
                setTextColor(MenuDesign.Colors.BUTTON_TEXT)
                gravity = Gravity.CENTER
                parent.addView(this)
            }
        }

        private fun getButtonBackground(): GradientDrawable {
            return GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = MenuDesign.Measurements.BUTTON_CORNER_RADIUS
                setColor(MenuDesign.Colors.BUTTON_BACKGROUND)
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
                setTextColor(MenuDesign.Colors.MAIN)
                textSize = MenuDesign.Measurements.TITLE_TEXT_SIZE
                parent.addView(this)
            }
        }


    }
}