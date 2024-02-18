package com.fiap.grainall.domain.state

sealed class RequestLoginState<out T> {
    object Success: RequestLoginState<Nothing>()
    object Fail: RequestLoginState<Nothing>()
}
