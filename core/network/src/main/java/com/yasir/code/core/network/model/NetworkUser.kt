package com.yasir.code.core.network.model

import com.squareup.moshi.Json
import com.yasir.code.core.domain.model.User

data class NetworkUser(
    @Json(name = "name")
    val name: String
)

fun NetworkUser.toUser() = User(
    name = name
)