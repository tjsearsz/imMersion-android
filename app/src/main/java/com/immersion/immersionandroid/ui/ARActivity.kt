package com.immersion.immersionandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.commit
import com.immersion.immersionandroid.R
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.utils.setFullScreen

@AndroidEntryPoint
class ARActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aractivity)

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
}