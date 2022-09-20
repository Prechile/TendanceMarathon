package com.tendance.marathon.models

data class paygateRequest(
    val amount: Int,
    val description: String,
    val fee: Int,
    val numero: String,
    val payGateToken: String,
    val serviceName: String
)