package com.immersion.immersionandroid.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import com.immersion.immersionandroid.R
import dagger.hilt.android.AndroidEntryPoint
import com.immersion.immersionandroid.presentation.BranchViewModel


@AndroidEntryPoint
class CreateBranchActivity : AppCompatActivity() {

    private val viewModel: BranchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val companyId = intent.extras!!.getString("companyId")!!

        viewModel.setCompanyId(companyId)
        setContentView(R.layout.activity_create_branch)
    }
}