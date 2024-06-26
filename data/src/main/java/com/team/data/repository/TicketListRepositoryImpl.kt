package com.team.data.repository

import com.team.data.network.ApiService
import com.team.domain.models.Ticket
import com.team.domain.repository.TicketListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class TicketListRepositoryImpl(val apiService: ApiService) : TicketListRepository {

    private val ticketList = MutableStateFlow<List<Ticket>>(emptyList())

    override fun getTicketList(): Flow<List<Ticket>> =
        flow<List<Ticket>> {
            ticketList.value = apiService.getTicketList().tickets
            ticketList.collect { emit(it) }
        }
}