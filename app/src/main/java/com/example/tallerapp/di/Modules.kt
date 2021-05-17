package com.example.tallerapp.di

import com.example.tallerapp.RedditTopConstants.Companion.BASE_URL
import com.example.tallerapp.model.api.RedditTopApi
import com.example.tallerapp.model.service.RedditApiService
import com.example.tallerapp.viewmodel.RedditTopViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object Modules {

    private val redditTopViewModelModule = module {
        viewModel { RedditTopViewModel(get()) }
    }

    private val redditTopApiModule = module {
        single {
            val retrofit: Retrofit = get()
            retrofit.create(RedditTopApi::class.java)
        }

        single {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }

    private val repositoryModule = module {
        single { RedditApiService(get()) }
    }

    val all: List<Module> = listOf(
        redditTopApiModule,
        repositoryModule,
        redditTopViewModelModule
    )

}