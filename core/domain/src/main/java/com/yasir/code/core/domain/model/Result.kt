package com.yasir.code.core.domain.model

// TODO: Handle invariant
sealed class Result<out T> {
    data class Success<T>(
        val data: T
    ) : Result<T>()

    data class Failure<T>(val error: Exception? = null, val message: String = "") : Result<T>()
}