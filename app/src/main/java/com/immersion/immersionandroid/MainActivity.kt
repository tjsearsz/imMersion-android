package com.immersion.immersionandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.ActivityMainBinding
import com.immersion.immersionandroid.databinding.FragmentJobImageBinding
import com.immersion.immersionandroid.presentation.MainViewModel
import com.immersion.immersionandroid.ui.CreateUserActivity
import com.immersion.immersionandroid.ui.FragmentAR
import dagger.hilt.android.AndroidEntryPoint
import io.github.sceneview.utils.setFullScreen
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
                viewModel.logIn(
                    binding.editTextTextEmailAddress.text.toString(),
                    binding.editTextTextPassword.text.toString()
                )

                lifecycleScope.launch(Dispatchers.Main){
                    Toast.makeText(applicationContext, "LOGGED SUCCESSFULLY", Toast.LENGTH_SHORT).show()
                }
            }

        }

        binding.register.setOnClickListener {

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