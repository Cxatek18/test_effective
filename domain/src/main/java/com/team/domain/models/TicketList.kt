package com.team.domain.models

import com.google.gson.annotations.SerializedName

data class TicketList(
    @SerializedName("tickets")
    val tickets: List<Ticket>
)
