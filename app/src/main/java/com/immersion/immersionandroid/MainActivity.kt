package com.immersion.immersionandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.ActivityMainBinding
import com.immersion.immersionandroid.presentation.MainViewModel
import com.immersion.immersionandroid.ui.ARActivity
import com.immersion.immersionandroid.ui.CreateUserActivity
import com.immersion.immersionandroid.ui.OwnershipActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val viewModel: MainViewModel by viewModels()
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        initializeButtons()

        setContentView(view)
    }

    private fun initializeButtons() {
        binding.logIn.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                val isBusinessOwner = viewModel.logIn(
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextTextPassword.text.toString()
                )

                lifecycleScope.launch(Dispatchers.Main) {

                    when (isBusinessOwner) {
                        true -> Intent(applicationContext, OwnershipActivity::class.java).also {
                            startActivity(it)
                        }

                        false -> Intent(applicationContext, ARActivity::class.java).also {
                            startActivity(it)
                        }

                        else -> Toast.makeText(
                            applicationContext,
                            "There was an error while logging in",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                }

            }
        }

        binding.register.setOnClickListener {
            Log.d("testing", "aca voy!!!!")
            Intent(applicationContext, CreateUserActivity::class.java).also {
                startActivity(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}