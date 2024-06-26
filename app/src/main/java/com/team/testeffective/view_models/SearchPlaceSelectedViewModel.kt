package com.team.testeffective.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team.domain.models.RecommendedOffer
import com.team.domain.usecase.GetRecommendedOfferListUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class SearchPlaceSelectedViewModel(
    val getRecommendedOfferListUseCase: GetRecommendedOfferListUseCase
) : ViewModel() {

    private val _recommendedOfferList =
        MutableStateFlow<List<RecommendedOffer>>(emptyList())
    val recommendedOfferList: StateFlow<List<RecommendedOffer>> =
        _recommendedOfferList.asStateFlow()

    private val _isOnline = Channel<Boolean>(Channel.CONFLATED)
    val isOnline = _isOnline.receiveAsFlow()

    fun getRecommendedOfferList() {
        viewModelScope.launch {
            getRecommendedOfferListUseCase.getRecommendedOfferList()
                .catch {
                    when (it) {
                        is UnknownHostException,
                        is SocketTimeoutException -> _isOnline.send(false)
                    }
                }
                .collect { recommendedOfferList ->
                    _recommendedOfferList.value = recommendedOfferList
                }
        }
    }
}