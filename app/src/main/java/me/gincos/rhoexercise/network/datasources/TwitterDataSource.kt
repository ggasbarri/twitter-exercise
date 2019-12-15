package me.gincos.rhoexercise.network.datasources

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import me.gincos.rhoexercise.network.FetchingState
import me.gincos.rhoexercise.network.RetrofitClient
import io.reactivex.schedulers.Schedulers
import com.squareup.moshi.Moshi
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import me.gincos.rhoexercise.network.responses.Status
import java.io.IOException
import okio.BufferedSource


class TwitterDataSource(private val retrofitClient: RetrofitClient) {

    private val moshi = Moshi.Builder().build()
    private val jsonAdapter = moshi.adapter<Status>(Status::class.java)
    val fetchingState = MutableLiveData<FetchingState>(FetchingState.Idle)

    private var currentDisposable: Disposable? = null

    fun getStatus(consumer: Consumer<Status>, track: String): Disposable {
        fetchingState.postValue(FetchingState.Fetching)

        if(currentDisposable != null){
            currentDisposable?.dispose()
        }

        currentDisposable = retrofitClient.apiService.statusFilter(track)
            .subscribeOn(Schedulers.io())
            .flatMap {
                statuses(it.source())
            }
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorResumeNext(Observable.empty())
            .subscribe(consumer)
        return currentDisposable as Disposable
    }

    private fun statuses(source: BufferedSource): Observable<Status> {
        return Observable.create { emitter ->
            try {
                while (!source.exhausted()) {
                    val current = source.readUtf8Line()
                    if (current != null) {
                        emitter.onNext(jsonAdapter.fromJson(current)!!)
                    }
                }
                emitter.onComplete()
            } catch (e: IOException) {
                e.printStackTrace()
                emitter.onError(e)
            }
        }
    }
}
