package com.team.domain.models

import com.google.gson.annotations.SerializedName

data class OfferList(
    @SerializedName("offers")
    val offerList: List<Offer>
)
