package com.team.data.repository

import com.team.data.network.ApiService
import com.team.domain.models.RecommendedOffer
import com.team.domain.repository.TicketsOffersRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class TicketsOffersRepositoryImpl(val apiService: ApiService) : TicketsOffersRepository {

    private val recommendedOfferList = MutableStateFlow<List<RecommendedOffer>>(emptyList())

    override fun getRecommendedOfferList(): Flow<List<RecommendedOffer>> =
        flow<List<RecommendedOffer>> {
            recommendedOfferList.value = apiService.getRecommendedOfferList().ticketsOffers
            recommendedOfferList.collect { emit(it) }
        }
}