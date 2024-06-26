package com.team.domain.repository

import com.team.domain.models.Ticket
import kotlinx.coroutines.flow.Flow

interface TicketListRepository {

    fun getTicketList(): Flow<List<Ticket>>
}