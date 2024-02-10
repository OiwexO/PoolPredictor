package com.iwex.poolpredictor.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

// it's necessary to replace LiveData.smali and MutableLiveData.smali in
// 8bp/smali/androidx/lifecycle by files from iwex/androidx/lifecycle
// otherwise the menu will crash

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LauncherActivity.launch(this)
    }

}
