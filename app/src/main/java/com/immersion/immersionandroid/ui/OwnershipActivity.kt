package com.immersion.immersionandroid.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.ActivityOwnershipBinding
import com.immersion.immersionandroid.domain.Company
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OwnershipActivity : AppCompatActivity() {

    private var _binding: ActivityOwnershipBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityOwnershipBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)
    }

    fun setAddOwnershipFloatingButton(listener: View.OnClickListener){
        binding.floatingAddOwnership.setOnClickListener(listener)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}