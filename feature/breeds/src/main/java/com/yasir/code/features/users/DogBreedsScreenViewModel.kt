package com.yasir.code.features.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasir.code.core.domain.GetDogBreedsUseCase
import com.yasir.code.features.users.model.DogBreedsScreenUiState
import com.yasir.code.features.users.model.UsersScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    private val _usersState: MutableStateFlow<DogBreedsScreenUiState> = MutableStateFlow<DogBreedsScreenUiState>(DogBreedsScreenUiState.Loading)
    val usersState: StateFlow<DogBreedsScreenUiState> = _usersState

    init {
        viewModelScope.launch {
            getDogBreedsUseCase.invoke(viewModelScope)
                .map(dogBreedsScreenUiStateMapper::map)
                .catch { e: Throwable ->
                    // TODO: Fix
                    _usersState.value = DogBreedsScreenUiState.Error(e.message ?: "")
                }
                .collect {
                    _usersState.value = it
                }
        }

    }

}