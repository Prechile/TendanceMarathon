package com.tendance.marathon.models

data class EventPrice(
    val amount: Int,
    val criteria: String,
    val eventId: Int,
    val eventName: String,
    val priceId: Int,
    val state: Int
)