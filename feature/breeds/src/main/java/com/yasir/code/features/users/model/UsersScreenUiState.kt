package com.yasir.code.features.users.model

sealed class UsersScreenUiState {

    data class UsersUiState(
        val users: List<UserUiState>
    ) : UsersScreenUiState()

    object Loading : UsersScreenUiState()

    data class Error(val message: String) : UsersScreenUiState()
}