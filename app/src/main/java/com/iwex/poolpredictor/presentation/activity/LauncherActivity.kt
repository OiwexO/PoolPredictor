package com.iwex.poolpredictor.presentation.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.lifecycle.Observer
import com.iwex.poolpredictor.di.factory.ViewModelFactory
import com.iwex.poolpredictor.presentation.resource.Strings
import com.iwex.poolpredictor.presentation.service.PredictorService
import com.iwex.poolpredictor.presentation.viewmodel.LauncherViewModel

class LauncherActivity : Activity() {
    private val viewModel: LauncherViewModel by lazy {
        ViewModelFactory.getInstance(this).launcherViewModel
    }
    private val isTableSetObserver = Observer<Boolean> {
        startPredictorService(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleOverlayPermission()
    }

    private fun handleOverlayPermission() {
        if (isOverlayPermissionProvided) {
            observeViewModel()
        } else {
            requestOverlayPermission()
        }
    }

    private val isOverlayPermissionProvided: Boolean
        get() = Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(this)

    @SuppressLint("InlinedApi")
    private fun requestOverlayPermission() {
        Toast.makeText(this, Strings.TOAST_ENABLE_OVERLAY_PERMISSION, Toast.LENGTH_LONG).show()
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${packageName}")
        )
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            handleOverlayPermission()
        }
    }

    private fun observeViewModel() {
        viewModel.isTableSet.observeForever(isTableSetObserver)
        viewModel.getIsTableSet()
    }

    private fun startPredictorService(isTableSet: Boolean) {
        val intent = PredictorService.newIntent(this, isTableSet)
        startService(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.isTableSet.removeObserver(isTableSetObserver)
    }

    companion object {
        private const val REQUEST_OVERLAY_PERMISSION = 1001

        fun launch(context: Context) {
            if (context !is Activity) {
                throw IllegalArgumentException("launch() requires activity context")
            }
            val intent = Intent(context, LauncherActivity::class.java)
            context.startActivity(intent)
        }
    }
}
