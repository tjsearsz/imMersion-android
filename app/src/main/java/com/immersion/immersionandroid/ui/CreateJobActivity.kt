package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

        binding.createJobButton.setOnClickListener {
            viewModel.addJobNameAndDescription(
                binding.jobTitle.text.toString(),
                binding.jobDescription.text.toString()
            )

            lifecycleScope.launch(Dispatchers.IO) {
                val newJob = viewModel.createNewJob(branchId)
                lifecycleScope.launch(Dispatchers.Main) {
                    if (newJob != null) {
                        val resultIntent = Intent().putExtra("entity", newJob)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        }

        binding.jobTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                binding.createJobButton.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                TODO("Not yet implemented")
            }

            override fun afterTextChanged(s: Editable?) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}