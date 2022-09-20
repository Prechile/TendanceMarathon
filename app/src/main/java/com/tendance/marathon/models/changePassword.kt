package com.tendance.marathon.models

data class changePassword(
    val newPassword: String,
    val oldPassword: String
)