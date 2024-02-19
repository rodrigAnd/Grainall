package com.fiap.grainall.domain.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser

interface LoginRepository {
    fun addUsuario(user: User)
    fun getUserLogged(): User
    suspend fun login(user: User): RequestState<FirebaseUser>
    fun logout()
}