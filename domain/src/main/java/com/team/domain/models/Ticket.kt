package com.team.domain.models

import com.google.gson.annotations.SerializedName

data class Ticket(
    @SerializedName("id")
    val id: Int,
    @SerializedName("badge")
    val badge: String?,
    @SerializedName("price")
    val price: Price,
    @SerializedName("departure")
    val departure: Departure,
    @SerializedName("arrival")
    val arrival: Arrival,
    @SerializedName("has_transfer")
    val hasTransfer: Boolean
)
