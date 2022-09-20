package com.tendance.marathon.models

data class newClientRequest(
    val name: String? ="",
    val phoneNumber: String?="",
    val country: String?="",
    val birthDate: String?="",
    val gender: Int?=0,
    val emergencyPersonName: String?="",
    val emergencyPersonPhoneNumber: String?="",
    val eventId: Int?=0,
    val modeDePayement: Int?=0,




)