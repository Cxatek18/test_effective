package com.team.testeffective.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.models.Ticket
import com.team.domain.usecase.GetTicketListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ShowAllTicketsViewModel(
    val getTicketListUseCase: GetTicketListUseCase
) : ViewModel() {

    private val _ticketList =
        MutableStateFlow<List<Ticket>>(emptyList())
    val ticketList: StateFlow<List<Ticket>> =
        _ticketList.asStateFlow()

    private val _isOnline = Channel<Boolean>(Channel.CONFLATED)
    val isOnline = _isOnline.receiveAsFlow()

    fun getTicketList() {
        viewModelScope.launch {
            getTicketListUseCase.getTicketList()
                .catch {
                    when (it) {
                        is UnknownHostException,
                        is SocketTimeoutException -> _isOnline.send(false)
                    }
                }
                .collect { ticketList ->
                    _ticketList.value = ticketList
                }
        }
    }
}