package com.iwex.poolpredictor

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.iwex.poolpredictor.app.Launcher

// it's necessary to replace LiveData.smali and MutableLiveData.smali in
// 8bp/smali/androidx/lifecycle by files from iwex/androidx/lifecycle
// otherwise the menu will crash

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Launcher.launchService(this)
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_OVERLAY_PERMISSION) {
            if (isOverlayPermissionProvided()) {
                startService()
            } else {
                Toast.makeText(this, "Overlay Permission not granted", Toast.LENGTH_LONG).show()
            }
        }
    }*/
}
