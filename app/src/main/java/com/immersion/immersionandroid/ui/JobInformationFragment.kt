package com.immersion.immersionandroid.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.FragmentJobInformationBinding
import com.immersion.immersionandroid.presentation.JobViewModel

class JobInformationFragment : Fragment() {

    private var _binding: FragmentJobInformationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: JobViewModel by activityViewModels<JobViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentJobInformationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nextButton.setOnClickListener {
            viewModel.addJobNameAndDescription(
                binding.editTextText.text.toString(),
                binding.editTextTextMultiLine.text.toString()
            )
            val action =
                JobInformationFragmentDirections.actionJobInformationFragmentToJobImageFragment()
            view.findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}