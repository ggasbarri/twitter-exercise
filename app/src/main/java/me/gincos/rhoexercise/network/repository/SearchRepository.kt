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

    val fetchingState: LiveData<FetchingState>
        get() = dataSource.fetchingState

    fun search(consumer: Consumer<Status>, query: String) {
        dataSource.getStatus(consumer, query)
    }
}