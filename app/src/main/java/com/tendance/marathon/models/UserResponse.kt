package com.tendance.marathon.models

data class UserResponse(
    val agentId: Int? = 0,
    val agentOperations: List<Any>?,
    val agentTickets: List<AgentTicket>?,
    val assignDevices: List<Any>?,
    val code: String?,
    val haveAssignedDevice: Boolean?,
    val image: String?,
    val isActivated: Boolean?,
    val name: String?,
    val password: String?,
    val phoneNumber: String?,
    val token: String?
)