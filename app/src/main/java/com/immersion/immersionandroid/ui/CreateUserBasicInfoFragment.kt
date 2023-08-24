package com.immersion.immersionandroid.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.FragmentCreateUserBasicInfoBinding
import com.immersion.immersionandroid.presentation.JobViewModel
import com.immersion.immersionandroid.presentation.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [CreateUserBasicInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CreateUserBasicInfoFragment : Fragment() {

    private var _binding: FragmentCreateUserBasicInfoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateUserBasicInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonAddUserNext.setOnClickListener {

            lifecycleScope.launch(Dispatchers.IO) {
                //TODO: IMPLEMENT VALIDATIONS FOR BOTH FIELDS
                val isResultSuccessful = viewModel.registerUser(
                    binding.editTextAddEmail.text.toString(),
                    binding.editTextAddPassword.text.toString()
                )


                lifecycleScope.launch(Dispatchers.Main) {
                    Log.d("TESTING", "AQUI VOY!!!!!!! $isResultSuccessful")

                    if (isResultSuccessful) {
                        val action =
                            CreateUserBasicInfoFragmentDirections
                                .actionCreateUserBasicInfoFragmentToCreateUserIntentFragment()
                        view.findNavController().navigate(action)
                    } else
                        Toast.makeText(
                            context,
                            "There was an error. Please try again later",
                            Toast.LENGTH_SHORT
                        ).show()
                }

            }


        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}