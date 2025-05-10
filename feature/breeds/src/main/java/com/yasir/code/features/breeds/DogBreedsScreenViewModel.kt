package com.yasir.code.features.breeds

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasir.code.core.domain.GetDogBreedsUseCase
import com.yasir.code.features.breeds.model.DogBreedsScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DogBreedsScreenViewModel @Inject constructor(
    private val getDogBreedsUseCase: GetDogBreedsUseCase,
    private val dogBreedsScreenUiStateMapper: DogBreedsScreenUiStateMapper
) : ViewModel() {

    private val _breedsState: MutableStateFlow<DogBreedsScreenUiState> =
        MutableStateFlow(DogBreedsScreenUiState.Loading)
    val breedsState: StateFlow<DogBreedsScreenUiState> = _breedsState

    init {
        performLoad()
    }

    @VisibleForTesting
    fun performLoad() {
        viewModelScope.launch {
            getDogBreedsUseCase.invoke(viewModelScope)
                .map(dogBreedsScreenUiStateMapper::map)
                .catch { e: Throwable ->
                    _breedsState.value = dogBreedsScreenUiStateMapper.mapError(e)
                }
                .collect {
                    _breedsState.value = it
                }
        }
    }
}