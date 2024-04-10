package com.iwex.poolpredictor.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val threadPolicy = StrictMode.ThreadPolicy.Builder()
//            .detectAll()
//            .penaltyLog()
//            .build()
//        StrictMode.setThreadPolicy(threadPolicy)
//        val vmPolicy = StrictMode.VmPolicy.Builder()
//            .detectAll()
//            .penaltyLog()
//            .build()
//        StrictMode.setVmPolicy(vmPolicy)
        LauncherActivity.launch(this)
    }

}
