package me.gincos.rhoexercise.network.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.functions.Consumer
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gincos.rhoexercise.TWEET_LIFESPAN_SECS
import me.gincos.rhoexercise.network.FetchingState
import me.gincos.rhoexercise.network.datasources.TwitterDataSource
import me.gincos.rhoexercise.network.responses.Status
import java.util.*

class SearchRepository(private val dataSource: TwitterDataSource) {

    val searchResults = MutableLiveData<ArrayDeque<Status>>(ArrayDeque())

    val fetchingState: LiveData<FetchingState>
        get() = dataSource.fetchingState

    fun search(query: String) {
        val consumer = Consumer<Status> {
            GlobalScope.launch {
                val queueBeforeAdding = searchResults.value

                queueBeforeAdding?.add(it)

                searchResults.postValue(queueBeforeAdding)

                delay(TWEET_LIFESPAN_SECS * 1000L)

                val queueAfterAdding = searchResults.value

                queueAfterAdding?.pop()

                searchResults.postValue(queueAfterAdding)
            }
        }

        dataSource.getStatus(consumer, query)
    }
}