package com.yasir.code.core.network

import com.yasir.code.core.domain.model.Result
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class NetworkResponseHandler @Inject constructor() {
    suspend fun <T> handle(networkCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response: Response<T> = networkCall()
            if (response.isSuccessful) {
                Result.Success(
                    response.body()
                        ?: throw java.lang.IllegalStateException("Response without body")
                )
            } else {
                Result.Failure(message = response.message())
            }
        } catch (e: IOException) {
            Result.Failure(error = e)
        }
    }
}