package me.gincos.rhoexercise.network

sealed class FetchingState {
    object Idle : FetchingState()
    object Fetching : FetchingState()
    object NetworkError : FetchingState()
    object ResponseError : FetchingState()
}