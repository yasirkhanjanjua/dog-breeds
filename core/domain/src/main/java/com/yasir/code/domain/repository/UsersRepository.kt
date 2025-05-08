package com.yasir.code.domain.repository

import com.yasir.code.domain.model.Result
import com.yasir.code.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun fetchUsers(): Flow<Result<List<User>>>
}