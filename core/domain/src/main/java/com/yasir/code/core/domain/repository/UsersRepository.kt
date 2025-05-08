package com.yasir.code.core.domain.repository

import com.yasir.code.core.domain.model.Result
import com.yasir.code.core.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UsersRepository {
    fun fetchUsers(): Flow<Result<List<User>>>
}