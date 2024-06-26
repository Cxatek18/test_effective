package com.team.domain.repository

import com.team.domain.models.RecommendedOffer
import kotlinx.coroutines.flow.Flow

interface TicketsOffersRepository {

    fun getRecommendedOfferList(): Flow<List<RecommendedOffer>>
}