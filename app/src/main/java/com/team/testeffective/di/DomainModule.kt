package com.team.testeffective.di

import com.team.domain.usecase.GetOfferListUseCase
import com.team.domain.usecase.GetRecommendedOfferListUseCase
import com.team.domain.usecase.GetTicketListUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetOfferListUseCase> {
        GetOfferListUseCase(repository = get())
    }

    factory<GetRecommendedOfferListUseCase> {
        GetRecommendedOfferListUseCase(repository = get())
    }

    factory<GetTicketListUseCase> {
        GetTicketListUseCase(repository = get())
    }
}