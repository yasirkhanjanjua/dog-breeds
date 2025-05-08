package com.yasir.code.core.network

import com.yasir.code.core.network.model.NetworkUser
import com.yasir.code.domain.model.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun fetchUsers(): Response<List<NetworkUser>>
}