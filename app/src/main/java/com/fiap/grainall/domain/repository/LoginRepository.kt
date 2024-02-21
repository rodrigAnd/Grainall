package com.fiap.grainall.domain.repository

import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
    suspend fun createUser(user: User): RequestState<FirebaseUser>
    fun getUserLogged(): User
    suspend fun login(user: User): RequestState<FirebaseUser>
}