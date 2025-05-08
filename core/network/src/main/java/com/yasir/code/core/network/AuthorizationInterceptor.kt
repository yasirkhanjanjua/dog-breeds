package com.yasir.code.core.network

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import javax.inject.Inject

class AuthorizationInterceptor @Inject constructor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val newRequest: Request = originalRequest.newBuilder()
            .addHeader(
                HEADER_API_KEY,
                ""
            )
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val HEADER_API_KEY = "api-key"
    }
}