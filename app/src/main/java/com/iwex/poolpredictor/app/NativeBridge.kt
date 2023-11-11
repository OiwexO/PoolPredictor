package com.iwex.poolpredictor.app

import android.content.Context
import com.iwex.poolpredictor.app.view.EspView

@Suppress("KotlinJniMissingFunction")
class NativeBridge {
    companion object {
        init {
            System.loadLibrary("poolpredictor")
        }

        //TODO obfuscate names with @JvmName("name")

        // AimTabViewModel native methods
        external fun setDrawLines(value: Boolean)
        external fun setDrawShotState(value: Boolean)
        external fun setDrawOpponentsLines(value: Boolean)
        external fun setPowerControlModeEnabled(value: Boolean)
        external fun setCuePower(power: Int)
        external fun setCueSpin(spin: Int)

        // OtherTabViewModel native methods
        external fun exitThread()

        // FloatingMenuLayout native methods
        external fun getIcon(): String

        // FloatingMenuService native methods
        external fun setServiceContext(context: Context)
        external fun setEspView(espView: EspView)
        external fun getPocketPositionsInScreen(left: Int, top: Int, right: Int, bottom: Int): FloatArray
    }

}
