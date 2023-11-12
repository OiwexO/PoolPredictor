package com.iwex.poolpredictor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iwex.poolpredictor.app.LauncherActivity

// it's necessary to replace LiveData.smali and MutableLiveData.smali in
// 8bp/smali/androidx/lifecycle by files from iwex/androidx/lifecycle
// otherwise the menu will crash

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LauncherActivity.launch(this)
    }

}
