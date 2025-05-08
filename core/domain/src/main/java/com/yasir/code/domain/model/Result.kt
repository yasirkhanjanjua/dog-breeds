package com.yasir.code.domain.model

// TODO: Handle invariant
sealed class Result<T> {
    data class Success<T>(
        val data: T
    ) : Result<T>()

    data class Failure<T>(val error: Exception? = null, val message: String = "") : Result<T>()
}