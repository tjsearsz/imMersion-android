package com.immersion.immersionandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.immersion.immersionandroid.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateJobActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_job)
    }
}