package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.Toast
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.service.PredictorService
import com.iwex.poolpredictor.presentation.viewmodel.OtherTabViewModel

@SuppressLint("ViewConstructor")
class OtherTab(
    context: Context,
    private val viewModel: OtherTabViewModel
) : BaseMenuTab(context, viewModel) {
    private val openYouTubeChannelButton: Button
    private val openGithubRepositoryButton: Button
    private val exitButton: Button

    init {
        openYouTubeChannelButton = MenuWidgetFactory.addButton(
            Strings.LABEL_OPEN_YOUTUBE_CHANNEL_BUTTON,
            true,
            context,
            this
        )
        openGithubRepositoryButton = MenuWidgetFactory.addButton(
            Strings.LABEL_OPEN_GITHUB_REPOSITORY_BUTTON,
            true,
            context,
            this
        )
        exitButton = MenuWidgetFactory.addButton(
            Strings.LABEL_EXIT_BUTTON,
            true,
            context,
            this
        )
        setButtonClickListeners()
    }

    private fun setButtonClickListeners() {
        openYouTubeChannelButton.setOnClickListener {
            Toast.makeText(context, Strings.TOAST_LONG_CLICK_TO_OPEN, Toast.LENGTH_LONG).show()
        }
        openYouTubeChannelButton.setOnLongClickListener {
            openUrl(Strings.YOUTUBE_CHANNEL_URL)
            true
        }
        openGithubRepositoryButton.setOnClickListener {
            Toast.makeText(context, Strings.TOAST_LONG_CLICK_TO_OPEN, Toast.LENGTH_LONG).show()
        }
        openGithubRepositoryButton.setOnLongClickListener {
            openUrl(Strings.GITHUB_REPOSITORY_URL)
            true
        }
        exitButton.setOnClickListener {
            Toast.makeText(context, Strings.TOAST_LONG_CLICK_TO_EXIT, Toast.LENGTH_LONG).show()
        }
        exitButton.setOnLongClickListener {
            viewModel.onExit()
            val serviceIntent = Intent(context, PredictorService::class.java)
            context.stopService(serviceIntent)
            true
        }
    }

    private fun openUrl(url: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        if (webIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(webIntent)
        }
    }
}
