package com.tendance.marathon.models

data class EventsResponse(
    val endAt: String,
    val eventCoverUrl: String,
    val eventDate: String,
    val eventHour: String,
    val eventId: Int,
    val eventOperations: Any,
    val eventPlace: String,
    val eventPrices: List<EventPrice>,
    val eventTickets: List<EventTicket>,
    val name: String,
    val startAt: String,
    val state: Int
)