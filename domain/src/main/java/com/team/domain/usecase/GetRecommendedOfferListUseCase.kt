package com.team.domain.usecase

import com.team.domain.models.RecommendedOffer
import com.team.domain.repository.TicketsOffersRepository
import kotlinx.coroutines.flow.Flow

class GetRecommendedOfferListUseCase(private val repository: TicketsOffersRepository) {

    fun getRecommendedOfferList(): Flow<List<RecommendedOffer>> {
        return repository.getRecommendedOfferList()
    }
}