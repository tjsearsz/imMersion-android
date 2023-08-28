package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.FragmentCreateCompanySectorBinding
import com.immersion.immersionandroid.presentation.CompanyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateCompanySectorFragment : Fragment() {

    private var _binding: FragmentCreateCompanySectorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompanyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCompanySectorBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gridLayout = binding.companySectorGrid

        val companySectorList = viewModel.retrieveCompanySectors()

        Log.d("debugging", "el tamaño al llegar aca ${companySectorList.size}")

        for (sector in companySectorList) {
            val button = Button(context)
            button.text = sector.title
            button.textSize = 18F
            button.layoutParams = GridLayout.LayoutParams().apply {
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            button.setOnClickListener {

                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val createdCompany = viewModel.createCompany(
                        sector.id
                    )

                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        if (createdCompany != null) {
                            val resultIntent = Intent().putExtra("entity", createdCompany)
                            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
                            requireActivity().finish()
                        }
                    }

                }


            }

            gridLayout.addView(button)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}