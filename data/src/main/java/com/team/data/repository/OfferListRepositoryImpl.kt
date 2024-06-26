package com.team.data.repository

import com.team.data.network.ApiService
import com.team.domain.models.Offer
import com.team.domain.repository.OfferListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class OfferListRepositoryImpl(val apiService: ApiService) : OfferListRepository {

    private val offerList = MutableStateFlow<List<Offer>>(emptyList())

    override fun getOfferList(): Flow<List<Offer>> =
        flow<List<Offer>> {
            offerList.value = apiService.getOfferList().offerList
            offerList.collect { emit(it) }
        }
}