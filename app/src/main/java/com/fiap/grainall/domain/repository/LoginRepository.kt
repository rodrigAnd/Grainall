package com.fiap.grainall.domain.repository

import com.fiap.grainall.domain.model.User

interface LoginRepository {
    fun addUsuario(user: User)
    fun getUserLogged(): User
    suspend fun login(user: User): Boolean
    fun logout()
}