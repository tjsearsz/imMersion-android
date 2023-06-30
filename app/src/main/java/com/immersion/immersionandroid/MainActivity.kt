package com.immersion.immersionandroid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import io.github.sceneview.utils.setFullScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setFullScreen(
            findViewById(R.id.mainView),
            fullScreen = true,
            hideSystemBars = false,
            fitsSystemWindows = false
        )

        supportFragmentManager.commit {
            add(
                R.id.fragmentContainerView,
                FragmentAR::class.java,
                Bundle()
            )
        }
    }
}