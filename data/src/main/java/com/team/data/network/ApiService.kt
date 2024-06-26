package com.team.data.network

import com.team.domain.models.OfferList
import com.team.domain.models.TicketList
import com.team.domain.models.TicketsOffers
import retrofit2.http.GET

interface ApiService {
    @GET("ad9a46ba-276c-4a81-88a6-c068e51cce3a")
    suspend fun getOfferList(): OfferList

    @GET("38b5205d-1a3d-4c2f-9d77-2f9d1ef01a4a")
    suspend fun getRecommendedOfferList(): TicketsOffers

    @GET("c0464573-5a13-45c9-89f8-717436748b69")
    suspend fun getTicketList(): TicketList
}