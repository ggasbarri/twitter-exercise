package me.gincos.rhoexercise.koin

import me.gincos.rhoexercise.main.MainAdapter
import me.gincos.rhoexercise.main.MainViewModel
import me.gincos.rhoexercise.network.RetrofitClient
import me.gincos.rhoexercise.network.datasources.TwitterDataSource
import me.gincos.rhoexercise.network.repository.SearchRepository
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val appModule = module {

    // Single Retrofit Client
    single { RetrofitClient() }

    // Single Twitter Data Source
    single { TwitterDataSource(get()) }

    // Single Search Repository
    single { SearchRepository(get()) }

    // MainActivity
    viewModel(name = MainViewModel.mainVmName) {
        MainViewModel(
            get()
        )
    }

    factory { MainAdapter() }
}