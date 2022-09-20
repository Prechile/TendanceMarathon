package com.tendance.marathon.models

data class LoginRequest(
    val emei: String,
    val password: String,
    val userName: String
)