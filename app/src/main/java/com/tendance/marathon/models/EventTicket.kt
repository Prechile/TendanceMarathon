package com.tendance.marathon.models

data class EventTicket(
    val agentId: Int,
    val agentName: String,
    val amount: Int,
    val clientCode: String,
    val clientName: String,
    val clientPhoneNumber: String,
    val date: String,
    val dossar: String,
    val eventDate: String,
    val eventName: String,
    val number: String,
    val type: String
)