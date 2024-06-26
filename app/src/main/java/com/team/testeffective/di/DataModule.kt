package com.team.testeffective.di

import com.team.data.network.ApiService
import com.team.data.repository.OfferListRepositoryImpl
import com.team.data.repository.TicketListRepositoryImpl
import com.team.data.repository.TicketsOffersRepositoryImpl
import com.team.domain.repository.OfferListRepository
import com.team.domain.repository.TicketListRepository
import com.team.domain.repository.TicketsOffersRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<OfferListRepository> {
        OfferListRepositoryImpl(apiService = get())
    }

    single<TicketsOffersRepository> {
        TicketsOffersRepositoryImpl(apiService = get())
    }

    single<TicketListRepository> {
        TicketListRepositoryImpl(apiService = get())
    }

    single<ApiService> {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://run.mocky.io/v3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        retrofit.create(ApiService::class.java)
    }
}