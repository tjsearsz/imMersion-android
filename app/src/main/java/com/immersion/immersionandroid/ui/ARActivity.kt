package com.immersion.immersionandroid.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.immersion.immersionandroid.R
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.utils.setFullScreen

@AndroidEntryPoint
class ARActivity : AppCompatActivity() {

    private val closeAr = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action === "CLOSE_AR")
                finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aractivity)

        registerReceiver(
            closeAr,
            IntentFilter("CLOSE_AR"),
        )

        setFullScreen(
            findViewById(R.id.arView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )

        supportFragmentManager.commit {
            add(
                R.id.ARfragmentContainerView,
                FragmentAR::class.java,
                Bundle()
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(closeAr)
    }
}