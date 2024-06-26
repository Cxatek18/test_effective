package com.team.domain.repository

import com.team.domain.models.Offer
import kotlinx.coroutines.flow.Flow

interface OfferListRepository {
    fun getOfferList(): Flow<List<Offer>>
}