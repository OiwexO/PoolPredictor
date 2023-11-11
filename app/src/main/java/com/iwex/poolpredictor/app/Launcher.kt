package com.iwex.poolpredictor.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import com.iwex.poolpredictor.app.service.FloatingMenuService

class Launcher {
    companion object {
        private const val TAG = "Launcher.kt"
        private const val TOAST_ENABLE_OVERLAY_PERMISSION = "Enable Overlay Permission"
        private const val LOG_WRONG_CONTEXT = "Wrong context has been passed: launchService() requires Activity context"
        private const val REQUEST_OVERLAY_PERMISSION = 1001

        fun launchService(context: Context) {
            if (context !is Activity) {
                Log.e(TAG, LOG_WRONG_CONTEXT)
                return
            }
            if (isOverlayPermissionProvided(context)) {
                startService(context)
            }
        }

        private fun isOverlayPermissionProvided(context: Context): Boolean {
            return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                true
            } else {
                if (Settings.canDrawOverlays(context)) {
                    true
                } else {
                    requestOverlayPermission(context)
                    false
                }
            }
        }

        @SuppressLint("InlinedApi")
        private fun requestOverlayPermission(context: Context) {
            Toast.makeText(context, TOAST_ENABLE_OVERLAY_PERMISSION, Toast.LENGTH_LONG).show()
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${context.packageName}"))
            if (context is Activity) {
                context.startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
            }
        }

        private fun startService(context: Context) {
            val intent = Intent(context, FloatingMenuService::class.java)
            if (context is Activity) {
                context.startService(intent)
            }
        }
    }
}
