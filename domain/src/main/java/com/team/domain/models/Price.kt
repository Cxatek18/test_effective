package com.team.domain.models

import com.google.gson.annotations.SerializedName

data class Price(
    @SerializedName("value")
    val value: Int
)
