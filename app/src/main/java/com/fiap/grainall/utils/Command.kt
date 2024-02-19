package com.fiap.grainall.utils



sealed class Resultado<out R> {
    data class Sucesso<T>(val dado: T? = null) : Resultado<T>()
    data class Erro(val exception: Exception) : Resultado<Nothing>()
}