package com.immersion.immersionandroid.ui

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.decodeBitmap
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.R
import com.immersion.immersionandroid.databinding.FragmentJobImageBinding
import com.immersion.immersionandroid.presentation.JobViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URI


/**
 * A simple [Fragment] subclass.
 * Use the [JobImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
// @ToDo: Try to update the forked filestack api to stop using onactivity result
class JobImageFragment : Fragment() {

    private var _binding: FragmentJobImageBinding? = null
    private val binding get() = _binding!!
    private var imageBitmap: Bitmap? = null
    private val viewModel: JobViewModel by activityViewModels<JobViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val branchId = requireActivity().intent.extras!!.getString("branchId")!!

        binding.createJobButton.setOnClickListener {

            val intendedURL = binding.imageUrl.text.toString().trim()

            Log.d("TESTING", intendedURL)
            var proceedWithCreation = true

            if (!TextUtils.isEmpty(intendedURL))
                proceedWithCreation = viewModel.validateUrl(intendedURL)


            if (proceedWithCreation)
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val newJob = viewModel.createNewJob(branchId)
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        if(newJob != null){
                            val intent = Intent().putExtra("entity", newJob)
                            requireActivity().setResult(Activity.RESULT_OK,intent)
                            requireActivity().finish()
                        }
                    }
                }
            else
                Toast.makeText(
                    context,
                    "URL inserted is not valid. Please fix it or remove it",
                    Toast.LENGTH_LONG
                ).show()

        }

        //@Todo: check in the code places where we should handle exceptions
        val pickJobPhoto = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
            this::handleImagePick
        )

        binding.selectImageButton.setOnClickListener {
            pickJobPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        pickJobPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handleImagePick(imageUri: Uri?) {
        if (imageUri != null) {
            Log.d("PhotoPicker", "Selected URI: $imageUri")

            val source = ImageDecoder.createSource(requireActivity().contentResolver, imageUri)
            imageBitmap = ImageDecoder.decodeBitmap(source) //@TODO: remember exceptions
            viewModel.addImageBitmap(ImageDecoder.decodeBitmap(source))
            binding.imageView.setImageURI(imageUri)

            binding.createJobButton.apply {
                isClickable = true
                isEnabled = true
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
            imageBitmap = null
            binding.imageView.setImageURI(null)
            viewModel.addImageBitmap(null)

            binding.createJobButton.apply {
                isClickable = false
                isEnabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}