package com.yasir.code.features.breeddetail

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasir.code.core.domain.GetDogBreedImagesUseCase
import com.yasir.code.core.domain.model.DogBreed
import com.yasir.code.features.breeddetail.model.DogBreedDetailScreenUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = DogBreedImagesScreenViewModelFactory::class)
class DogBreedImagesScreenViewModel @AssistedInject constructor(
    private val getDogBreedImagesUseCase: GetDogBreedImagesUseCase,
    private val dogBreedDetailScreenUiStateMapper: DogBreedDetailScreenUiStateMapper,
    @Assisted val breed: DogBreed,
) : ViewModel() {

    private val _uiState: MutableStateFlow<DogBreedDetailScreenUiState> =
        MutableStateFlow(
            DogBreedDetailScreenUiState.Loading(
                dogBreedDetailScreenUiStateMapper.mapTitle(
                    breed
                )
            )
        )
    val uiState: StateFlow<DogBreedDetailScreenUiState> = _uiState

    init {
        performLoad()
    }

    @VisibleForTesting
    fun performLoad() {
        viewModelScope.launch {
            getDogBreedImagesUseCase(breed)
                .map {
                    dogBreedDetailScreenUiStateMapper.map(breed, it)
                }
                .catch { e: Throwable ->
                    _uiState.value = dogBreedDetailScreenUiStateMapper.mapError(breed, e)
                }
                .collect {
                    _uiState.value = it
                }
        }
    }
}

@AssistedFactory
interface DogBreedImagesScreenViewModelFactory {
    fun create(breed: DogBreed): DogBreedImagesScreenViewModel
}