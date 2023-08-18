package com.immersion.immersionandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.FragmentCreateUserBasicInfoBinding
import com.immersion.immersionandroid.databinding.FragmentCreateUserIntentBinding
import com.immersion.immersionandroid.databinding.FragmentJobInformationBinding
import com.immersion.immersionandroid.presentation.JobViewModel
import com.immersion.immersionandroid.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateUserIntentFragment : Fragment() {

    private var _binding: FragmentCreateUserIntentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserIntentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.FinishButton.setOnClickListener {

            if (true)
                lifecycleScope.launch(Dispatchers.IO) {
                    viewModel.setUserAsBusinessOwner()
                    lifecycleScope.launch(Dispatchers.Main){
                        Toast.makeText(activity, "type changed", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}