package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.immersion.immersionandroid.databinding.FragmentCreateCompanyBasicInfoBinding
import com.immersion.immersionandroid.presentation.CompanyViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class CreateCompanyBasicInfoFragment : Fragment() {

    private var _binding: FragmentCreateCompanyBasicInfoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CompanyViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCompanyBasicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createCompanyNext.setOnClickListener {
           /* viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) { */

                val companyName = binding.companyName.text.toString()
                val companyDescription = binding.companyDescription.text.toString().trim()



                viewModel.setCompanyNameAndDescription(
                    companyName,
                    if (TextUtils.isEmpty(companyDescription)) null else companyDescription
                )

               /* viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {*/

                    val action = CreateCompanyBasicInfoFragmentDirections
                        .actionCreateCompanyBasicInfoFragment2ToCreateCompanySectorFragment()

                    view.findNavController().navigate(action)
               /* }
            }*/
        }


        addLogicForEnablingAddButton()
    }

    private fun addLogicForEnablingAddButton() {
        binding.companyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.createCompanyNext.isEnabled = s.toString().trim().isNotEmpty()
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