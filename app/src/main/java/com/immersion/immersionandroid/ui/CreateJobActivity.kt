package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.ActivityCreateJobBinding
import com.immersion.immersionandroid.presentation.JobViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateJobActivity : AppCompatActivity() {

    private var _binding: ActivityCreateJobBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCreateJobBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val branchId = intent.extras!!.getString("branchId")!!

        binding.nextButton.setOnClickListener {
            viewModel.addJobNameAndDescription(
                binding.editTextText.text.toString(),
                binding.editTextTextMultiLine.text.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                val newJob = viewModel.createNewJob(branchId)
                lifecycleScope.launch(Dispatchers.Main){
                    if(newJob != null){
                        val resultIntent = Intent().putExtra("entity", newJob)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}