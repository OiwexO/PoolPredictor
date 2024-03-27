package com.iwex.poolpredictor.data

import com.iwex.poolpredictor.domain.repository.NativeRepository

@Suppress("KotlinJniMissingFunction")
class NativeBridge {

    companion object {

        init {
            System.loadLibrary("poolpredictor")
        }

        @JvmStatic
        external fun updateAimSettings(
            drawLinesEnabled: Boolean,
            drawShotStateEnabled: Boolean,
            drawOpponentsLinesEnabled: Boolean,
            preciseTrajectoriesEnabled: Boolean,
            cuePower: Int,
            cueSpin: Int
        )

        @JvmStatic
        external fun exitThread()

        @JvmStatic
        external fun setNativeRepository(nativeRepository: NativeRepository)

        @JvmStatic
        external fun getPocketPositionsInScreen(left: Float, top: Float, right: Float, bottom: Float): FloatArray
    }

}
