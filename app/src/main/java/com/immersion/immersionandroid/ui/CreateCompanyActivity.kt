package com.immersion.immersionandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.ActivityCreateCompanyBinding
import com.immersion.immersionandroid.presentation.CompanyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCompanyActivity : AppCompatActivity() {

    private lateinit var _binding: ActivityCreateCompanyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityCreateCompanyBinding.inflate(layoutInflater)
        val view = _binding.root

        val viewModel: CompanyViewModel by viewModels()
        _binding.createCompany.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {

                val companyName = _binding.companyName.text.toString()
                val companyDescription = _binding.companyDescription.text.toString().trim()

                viewModel.createCompany(
                    companyName,
                    if (TextUtils.isEmpty(companyDescription)) null else companyDescription
                )
            }
        }

        addLogicForEnablingAddButton()

        setContentView(view)
    }

    private fun addLogicForEnablingAddButton() {
        _binding.companyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                _binding.createCompany.isEnabled = s.toString().trim().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}