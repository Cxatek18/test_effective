package com.team.domain.usecase

import com.team.domain.models.Ticket
import com.team.domain.repository.TicketListRepository
import kotlinx.coroutines.flow.Flow

class GetTicketListUseCase(private val repository: TicketListRepository) {

    fun getTicketList(): Flow<List<Ticket>> {
        return repository.getTicketList()
    }
}