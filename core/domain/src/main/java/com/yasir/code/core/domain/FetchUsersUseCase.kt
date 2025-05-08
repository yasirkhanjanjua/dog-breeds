package com.yasir.code.core.domain

import com.yasir.code.domain.model.Result
import com.yasir.code.domain.model.User
import com.yasir.code.domain.repository.UsersRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FetchUsersUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {
    operator fun invoke(): Flow<Result<List<User>>> = usersRepository.fetchUsers()
}