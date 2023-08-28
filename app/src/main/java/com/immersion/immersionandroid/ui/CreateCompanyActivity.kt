package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.ActivityCreateCompanyBinding
import com.immersion.immersionandroid.domain.Company
import com.immersion.immersionandroid.presentation.CompanyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateCompanyActivity : AppCompatActivity() {

    private val viewModel: CompanyViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_company)

        lifecycleScope.launch(Dispatchers.IO) {
            val success = viewModel.getAllCompanySectors()

            lifecycleScope.launch(Dispatchers.Main) {
                if (!success) {
                    Toast.makeText(
                        applicationContext,
                        "There was an error loading information, please try later",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                }
            }
        }
    }
}