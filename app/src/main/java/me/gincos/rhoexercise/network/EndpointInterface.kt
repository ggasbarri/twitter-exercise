package me.gincos.rhoexercise.network

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.Query
import retrofit2.http.POST
import retrofit2.http.Streaming


interface EndpointInterface {

    @Streaming
    @POST("statuses/filter.json")
    fun statusFilter(
        @Query("track") query: String
    )
            : Observable<ResponseBody>

}