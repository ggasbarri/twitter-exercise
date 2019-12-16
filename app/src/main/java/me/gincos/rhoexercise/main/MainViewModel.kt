package me.gincos.rhoexercise.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.gincos.rhoexercise.network.FetchingState
import me.gincos.rhoexercise.network.repository.SearchRepository
import me.gincos.rhoexercise.network.responses.Status
import java.util.*

class MainViewModel(private val repository: SearchRepository) : ViewModel() {

    val fetchingState: LiveData<FetchingState>
        get() = repository.fetchingState

    val searchResults: LiveData<ArrayDeque<Status>>
        get() = repository.searchResults

    fun search(query: String) {
        viewModelScope.launch {
            repository.search(query)
        }
    }

    companion object {
        const val mainVmName = "MainViewModel"
    }
}