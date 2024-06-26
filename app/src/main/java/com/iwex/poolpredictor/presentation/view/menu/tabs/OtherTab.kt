package com.iwex.poolpredictor.presentation.view.menu.tabs

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Button
import android.widget.Toast
import com.iwex.poolpredictor.data.NativeBridge
import com.iwex.poolpredictor.presentation.MenuWidgetFactory
import com.iwex.poolpredictor.presentation.service.PredictorService

class OtherTab(context: Context) : BaseMenuTab(context) {

    private val openYouTubeChannelButton: Button
    private val openGithubRepositoryButton: Button
    private val exitButton: Button

    init {
        openYouTubeChannelButton = MenuWidgetFactory.addButton(
            LABEL_OPEN_YOUTUBE_CHANNEL_BUTTON,
            true,
            context,
            this
        )
        openYouTubeChannelButton.setOnClickListener {
            Toast.makeText(context, TOAST_LONG_CLICK_TO_OPEN, Toast.LENGTH_LONG).show()
        }
        openYouTubeChannelButton.setOnLongClickListener {
            openUrl(context, YOUTUBE_CHANNEL_URL)
            true
        }
        openGithubRepositoryButton = MenuWidgetFactory.addButton(
            LABEL_OPEN_GITHUB_REPOSITORY_BUTTON,
            true,
            context,
            this
        )
        openGithubRepositoryButton.setOnClickListener {
            Toast.makeText(context, TOAST_LONG_CLICK_TO_OPEN, Toast.LENGTH_LONG).show()
        }
        openGithubRepositoryButton.setOnLongClickListener {
            openUrl(context, GITHUB_REPOSITORY_URL)
            true
        }
        exitButton = MenuWidgetFactory.addButton(
            LABEL_EXIT_BUTTON,
            true,
            context,
            this
        )
        exitButton.setOnClickListener {
            Toast.makeText(context, TOAST_LONG_CLICK_TO_EXIT, Toast.LENGTH_LONG).show()
        }
        exitButton.setOnLongClickListener {
            //TODO remove dependency on data layer
            NativeBridge.exitThread()
            val serviceIntent = Intent(context, PredictorService::class.java)
            context.stopService(serviceIntent)
            true
        }

    }

    private fun openUrl(context: Context, url: String) {
        val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        webIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        if (webIntent.resolveActivity(context.packageManager) != null) {
            context.startActivity(webIntent)
        }
    }

    companion object {

        private const val LABEL_OPEN_YOUTUBE_CHANNEL_BUTTON = "open my YouTube channel"
        private const val LABEL_OPEN_GITHUB_REPOSITORY_BUTTON = "open my GitHub repository"
        private const val LABEL_EXIT_BUTTON = "exit mod"
        private const val YOUTUBE_CHANNEL_URL = "https://www.youtube.com/@iwex"
        private const val GITHUB_REPOSITORY_URL = "https://github.com/OiwexO/PoolPredictor"
        private const val TOAST_LONG_CLICK_TO_EXIT = "hold button to exit mod"
        private const val TOAST_LONG_CLICK_TO_OPEN = "hold button to open link"
    }
}