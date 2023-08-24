package com.immersion.immersionandroid.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.immersion.immersionandroid.databinding.FragmentBranchImageBinding
import com.immersion.immersionandroid.presentation.BranchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


/**
 * A simple [Fragment] subclass.
 * Use the [BranchImageFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
// @ToDo: Try to update the forked filestack api to stop using onactivity result
class BranchImageFragment : Fragment() {

    private var _binding: FragmentBranchImageBinding? = null
    private val binding get() = _binding!!
    private var imageBitmap: Bitmap? = null
    private val viewModel: BranchViewModel by activityViewModels<BranchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBranchImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.createBranchButton.setOnClickListener {

            val intendedURL = binding.imageUrl.text.toString().trim()

            Log.d("TESTING", intendedURL)
            var proceedWithCreation = true

            if (!TextUtils.isEmpty(intendedURL))
                proceedWithCreation = viewModel.validateUrl(intendedURL)


            if (proceedWithCreation)
                viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                    val newBranch = viewModel.createBranch()
                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                        if(newBranch != null){
                            val intent = Intent().putExtra("entity", newBranch)
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
        val pickBranchPhoto = registerForActivityResult(
            ActivityResultContracts.PickVisualMedia(),
            this::handleImagePick
        )

        binding.selectImageButton.setOnClickListener {
            pickBranchPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        pickBranchPhoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun handleImagePick(imageUri: Uri?) {
        if (imageUri != null) {
            Log.d("PhotoPicker", "Selected URI: $imageUri")

            val source = ImageDecoder.createSource(requireActivity().contentResolver, imageUri)
            imageBitmap = ImageDecoder.decodeBitmap(source) //@TODO: remember exceptions
            viewModel.addImageBitmap(ImageDecoder.decodeBitmap(source))
            binding.imageView.setImageURI(imageUri)

            binding.createBranchButton.apply {
                isClickable = true
                isEnabled = true
            }
        } else {
            Log.d("PhotoPicker", "No media selected")
            imageBitmap = null
            binding.imageView.setImageURI(null)
            viewModel.addImageBitmap(null)

            binding.createBranchButton.apply {
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