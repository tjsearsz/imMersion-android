package com.immersion.immersionandroid.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.immersion.immersionandroid.dataAccess.ApolloAugmentedImageClient
import com.immersion.immersionandroid.domain.AugmentedImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/*@HiltViewModel
class AugmentedRealityViewModel @Inject constructor(private val apolloAugmentedImageClient: ApolloAugmentedImageClient): ViewModel() {

    private val _state: MutableStateFlow<AugmentedImagesState> = MutableStateFlow(AugmentedImagesState())
    val state: StateFlow<AugmentedImagesState> = _state.asStateFlow()

    init {
        Log.d("golazo", "supe megagaga")
        viewModelScope.launch {
            val result = apolloAugmentedImageClient.getAugmentedImages()
           // Log.d("metodo", result.modelURL)
            // _state.update { it.copy(augmentedImage = result) }
        }
    }

    data class AugmentedImagesState(val augmentedImage: AugmentedImage? = null)

}*/