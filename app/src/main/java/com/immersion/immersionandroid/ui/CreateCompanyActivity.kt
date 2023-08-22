package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.ActivityCreateCompanyBinding
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.presentation.CompanyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCompanyActivity : AppCompatActivity() {

    private var _binding: ActivityCreateCompanyBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCreateCompanyBinding.inflate(layoutInflater)
        val view = binding.root

        val viewModel: CompanyViewModel by viewModels()
        binding.createCompany.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                val companyName = binding.companyName.text.toString()
                val companyDescription = binding.companyDescription.text.toString().trim()

                val createdCompany = viewModel.createCompany(
                    companyName,
                    if (TextUtils.isEmpty(companyDescription)) null else companyDescription
                )
                lifecycleScope.launch(Dispatchers.Main) {
                    if (createdCompany != null) {
                        val resultIntent = Intent().putExtra("entity", createdCompany)
                        setResult(Activity.RESULT_OK, resultIntent)
                        finish()
                    }
                }
            }
        }

        addLogicForEnablingAddButton()

        setContentView(view)
    }

    private fun addLogicForEnablingAddButton() {
        binding.companyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createCompany.isEnabled = s.toString().trim().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}