package com.fiap.grainall.domain.state

sealed class RequestState<out T> {
    object Loading : RequestState<Nothing>()
    data class Success<T>(val data: T) : RequestState<T>()
    data class Error(val throwable: Throwable) : RequestState<Nothing>()
    data class NewActivity(val isRecord: Boolean) : RequestState<Nothing>()
}