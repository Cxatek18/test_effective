package com.team.domain.usecase

import com.team.domain.models.Offer
import com.team.domain.repository.OfferListRepository
import kotlinx.coroutines.flow.Flow

class GetOfferListUseCase(private val repository: OfferListRepository) {

    fun getOfferList(): Flow<List<Offer>> {
        return repository.getOfferList()
    }
}