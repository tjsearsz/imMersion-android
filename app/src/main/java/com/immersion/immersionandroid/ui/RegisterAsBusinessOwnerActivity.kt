package com.immersion.immersionandroid.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.ActivityRegisterAsBusinessOwnerBinding
import com.immersion.immersionandroid.presentation.UserViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterAsBusinessOwnerActivity : AppCompatActivity() {

    private var _binding: ActivityRegisterAsBusinessOwnerBinding? = null

    private val binding get() = _binding!!

    private val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityRegisterAsBusinessOwnerBinding.inflate(layoutInflater)
        val view = binding.root

        setContentView(view)

        binding.enableBusinessOwner.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                val isSuccessful = viewModel.makeUserBusinessOwner()

                lifecycleScope.launch(Dispatchers.Main) {
                    if (isSuccessful) {
                        sendBroadcast(Intent("CLOSE_AR"))

                        Intent(applicationContext, OwnershipActivity::class.java).also {
                            startActivity(it)
                            finish()
                        }
                    } else
                        Toast.makeText(
                            applicationContext,
                            "An error has occurred",
                            Toast.LENGTH_LONG
                        ).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}