package com.team.domain.models

import com.google.gson.annotations.SerializedName

data class TicketsOffers(
    @SerializedName("tickets_offers")
    val ticketsOffers: List<RecommendedOffer>
)
