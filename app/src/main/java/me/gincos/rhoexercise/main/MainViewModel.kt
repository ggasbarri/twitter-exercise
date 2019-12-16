package me.gincos.rhoexercise.main

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice.CONNECTED
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.gincos.rhoexercise.TWEET_LIFESPAN_SECS
import me.gincos.rhoexercise.network.FetchingState
import me.gincos.rhoexercise.network.repository.SearchRepository
import me.gincos.rhoexercise.network.responses.Status
import java.util.*

class MainViewModel(
    private val repository: SearchRepository
) : ViewModel() {

    val fetchingState: LiveData<FetchingState>
        get() = repository.fetchingState

    val searchResults = MutableLiveData<ArrayDeque<Status>>(ArrayDeque())

    var currentSearch: String = ""

    var hasActiveConnection = true

    lateinit var networkListenerDisposable: Disposable

    fun search(query: String) {
        currentSearch = query

        val consumer = Consumer<Status> {
            viewModelScope.launch {
                val queueBeforeAdding = searchResults.value

                queueBeforeAdding?.add(it)

                searchResults.postValue(queueBeforeAdding)

                clearQueueAfterDelay()
            }
        }

        viewModelScope.launch {
            repository.search(consumer, currentSearch)
        }
    }

    private suspend fun clearQueueAfterDelay() {

        delay(TWEET_LIFESPAN_SECS * 1000L)

        if (hasActiveConnection) {

            val queueAfterAdding = searchResults.value

            queueAfterAdding?.pop()

            searchResults.postValue(queueAfterAdding)
        } else {
            clearQueueAfterDelay()
        }
    }

    fun initNetworkListening(context: Context) {
        hasActiveConnection =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .isDefaultNetworkActive

        networkListenerDisposable = ReactiveNetwork
            .observeNetworkConnectivity(context)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                when (it.state()!!) {
                    NetworkInfo.State.CONNECTED -> {
                        if (!hasActiveConnection) {
                            search(currentSearch)
                        }
                        hasActiveConnection = true
                    }
                    NetworkInfo.State.CONNECTING,
                    NetworkInfo.State.DISCONNECTING,
                    NetworkInfo.State.SUSPENDED,
                    NetworkInfo.State.DISCONNECTED -> {
                        hasActiveConnection = false
                    }
                    NetworkInfo.State.UNKNOWN -> {
                        // Do nothing
                    }
                }
            }
    }

    companion object {
        const val mainVmName = "MainViewModel"
    }

    override fun onCleared() {
        super.onCleared()
        networkListenerDisposable.dispose()
    }
}