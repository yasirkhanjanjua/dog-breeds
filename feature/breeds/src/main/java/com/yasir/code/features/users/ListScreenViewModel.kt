package com.yasir.code.features.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yasir.code.core.domain.FetchUsersUseCase
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
class ListScreenViewModel @Inject constructor(
    private val fetchUsersUseCase: FetchUsersUseCase,
    private val dogBreedsScreenUiStateMapper: DogBreedsScreenUiStateMapper
) : ViewModel() {

    private val _usersState: MutableStateFlow<UsersScreenUiState> = MutableStateFlow<UsersScreenUiState>(UsersScreenUiState.Loading)
    val usersState: StateFlow<UsersScreenUiState> = _usersState

    init {
        viewModelScope.launch {
            fetchUsersUseCase()
                .map(dogBreedsScreenUiStateMapper::map)
                .catch { e: Throwable ->
                    _usersState.value = UsersScreenUiState.Error(e.message ?: "")
                }
                .collect {
                    delay(3000)
                    _usersState.value = it
                }
        }
    }

}