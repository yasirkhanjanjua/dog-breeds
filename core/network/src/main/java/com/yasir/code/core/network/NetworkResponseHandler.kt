package com.yasir.code.core.network

import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class NetworkResponseHandler @Inject constructor() {
    suspend fun <T> handle(networkCall: suspend () -> Response<T>): com.yasir.code.domain.model.Result<T> {
        return try {
            val response: Response<T> = networkCall()
            if (response.isSuccessful) {
                com.yasir.code.domain.model.Result.Success(
                    response.body()
                        ?: throw java.lang.IllegalStateException("Response without body")
                )
            } else {
                com.yasir.code.domain.model.Result.Failure(message = response.message())
            }
        } catch (e: IOException) {
            com.yasir.code.domain.model.Result.Failure(error = e)
        }
    }
}