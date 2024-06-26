package com.team.testeffective.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.models.Offer
import com.team.domain.usecase.GetOfferListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class MainViewModel(
    val getOfferListUseCase: GetOfferListUseCase
) : ViewModel() {

    private val _offerList = MutableStateFlow<List<Offer>>(emptyList())
    val offerList: StateFlow<List<Offer>> = _offerList.asStateFlow()

    private val _isOnline = Channel<Boolean>(Channel.CONFLATED)
    val isOnline = _isOnline.receiveAsFlow()

    fun getOfferList() {
        viewModelScope.launch {
            getOfferListUseCase.getOfferList()
                .catch {
                    when (it) {
                        is UnknownHostException,
                        is SocketTimeoutException -> _isOnline.send(false)
                    }
                }
                .collect { offerList ->
                    _offerList.value = offerList
                }
        }
    }
}