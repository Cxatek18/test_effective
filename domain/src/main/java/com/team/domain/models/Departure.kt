package com.team.domain.models

import com.google.gson.annotations.SerializedName

data class Departure(
    @SerializedName("town")
    val town: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("airport")
    val airport: String
)
