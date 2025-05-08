package com.yasir.code.core.network

import com.yasir.code.core.network.model.NetworkUser
import com.yasir.code.core.domain.model.Result
import javax.inject.Inject

class UsersNetworkDataSource @Inject constructor(
    private val apiService: ApiService,
    private val networkResponseHandler: NetworkResponseHandler
) {
    suspend fun fetchUsers(): Result<List<NetworkUser>> {
        return networkResponseHandler.handle {
            apiService.fetchUsers()
        }
    }
}