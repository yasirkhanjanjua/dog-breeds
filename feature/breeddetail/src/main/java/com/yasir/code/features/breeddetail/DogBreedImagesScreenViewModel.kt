package com.yasir.code.features.breeddetail

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
import javax.inject.Inject

@HiltViewModel(assistedFactory = DogBreedImagesScreenViewModelFactory::class)
class DogBreedImagesScreenViewModel @AssistedInject constructor(
    private val getDogBreedImagesUseCase: GetDogBreedImagesUseCase,
    private val dogBreedDetailScreenUiStateMapper: DogBreedDetailScreenUiStateMapper,
    @Assisted val breed: DogBreed,
) : ViewModel() {

    private val _usersState: MutableStateFlow<DogBreedDetailScreenUiState> =
        MutableStateFlow<DogBreedDetailScreenUiState>(
            DogBreedDetailScreenUiState.Loading
        )
    val usersState: StateFlow<DogBreedDetailScreenUiState> = _usersState

    init {
        viewModelScope.launch {
            getDogBreedImagesUseCase(breed)
                .map(dogBreedDetailScreenUiStateMapper::map)
                .catch { e: Throwable ->
                    // TODO: Fix
                    _usersState.value = DogBreedDetailScreenUiState.Error(e.message ?: "")
                }
                .collect {
                    _usersState.value = it
                }
        }
    }
}

@AssistedFactory
interface DogBreedImagesScreenViewModelFactory {
    fun create(breed: DogBreed): DogBreedImagesScreenViewModel
}