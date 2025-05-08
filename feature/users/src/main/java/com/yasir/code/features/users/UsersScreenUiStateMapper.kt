package com.yasir.code.features.users

import androidx.annotation.VisibleForTesting
import com.yasir.code.domain.model.Result
import com.yasir.code.domain.model.User
import com.yasir.code.features.users.model.UserUiState
import com.yasir.code.features.users.model.UsersScreenUiState
import javax.inject.Inject

class UsersScreenUiStateMapper @Inject constructor() {
    fun map(result: Result<List<User>>): UsersScreenUiState =
        when (result) {
            is Result.Success -> mapSuccess(result)
            is Result.Failure -> mapError(result)
        }

    @VisibleForTesting
    fun mapSuccess(result: Result.Success<List<User>>): UsersScreenUiState =
        UsersScreenUiState.UsersUiState(
            result.data.map {
                UserUiState(
                    name = it.name
                )
            })

    @VisibleForTesting
    fun mapError(result: Result.Failure<List<User>>): UsersScreenUiState =
        UsersScreenUiState.Error(result.message)
}