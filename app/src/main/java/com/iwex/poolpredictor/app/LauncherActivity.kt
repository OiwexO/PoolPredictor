package com.iwex.poolpredictor.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.iwex.poolpredictor.app.service.PredictorService
import com.iwex.poolpredictor.app.util.MenuDesign

class LauncherActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isOverlayPermissionProvided) {
            startService()
        }
    }

    private val isOverlayPermissionProvided: Boolean
        get() {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                true
            } else {
                if (Settings.canDrawOverlays(this)) {
                    true
                } else {
                    requestOverlayPermission()
                    false
                }
            }
        }

    @SuppressLint("InlinedApi")
    private fun requestOverlayPermission() {
        Toast.makeText(this, TOAST_ENABLE_OVERLAY_PERMISSION, Toast.LENGTH_LONG).show()
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${packageName}")
        )
        startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
    }

    private fun startService() {
        val intent = Intent(this, PredictorService::class.java)
        startService(intent)
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (isOverlayPermissionProvided) {
                startService()
            } else {
                Toast.makeText(this, TOAST_ENABLE_OVERLAY_PERMISSION, Toast.LENGTH_LONG).show()
            }
        }
    }

    companion object {
        private const val TAG = "LauncherActivity"
        private const val TOAST_ENABLE_OVERLAY_PERMISSION = "Please enable Overlay Permission"
        private const val LOG_WRONG_CONTEXT = "launch() requires activity context"
        private const val REQUEST_OVERLAY_PERMISSION = 1001

        fun launch(context: Context) {
            if (context !is Activity) {
                Log.e(TAG, LOG_WRONG_CONTEXT)
                return
            }
            MenuDesign.initDisplayMetrics(context)
            val intent = Intent(context, LauncherActivity::class.java)
            context.startActivity(intent)
        }

    }
}
