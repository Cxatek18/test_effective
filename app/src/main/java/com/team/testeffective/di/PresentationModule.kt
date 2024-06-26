package com.team.testeffective.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import com.team.testeffective.view_models.MainViewModel
import com.team.testeffective.view_models.SearchPlaceSelectedViewModel
import com.team.testeffective.view_models.ShowAllTicketsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {

    single<Cicerone<Router>> {
        Cicerone.create()
    }

    single<NavigatorHolder> {
        get<Cicerone<Router>>().getNavigatorHolder()
    }

    single<Router> {
        get<Cicerone<Router>>().router
    }

    viewModel<MainViewModel> {
        MainViewModel(getOfferListUseCase = get())
    }

    viewModel<SearchPlaceSelectedViewModel> {
        SearchPlaceSelectedViewModel(getRecommendedOfferListUseCase = get())
    }

    viewModel<ShowAllTicketsViewModel> {
        ShowAllTicketsViewModel(getTicketListUseCase = get())
    }
}