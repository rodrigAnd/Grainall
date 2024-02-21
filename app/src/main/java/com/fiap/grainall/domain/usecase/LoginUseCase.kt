package com.fiap.grainall.domain.usecase

import androidx.lifecycle.LiveData
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.repository.LoginRepository
import com.fiap.grainall.domain.state.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser


class LoginUseCase(private val loginRepositoty: LoginRepository) {
    suspend fun login(user: User): RequestState<FirebaseUser> {
        return loginRepositoty.login(user)
    }

    suspend fun createUser(email: String, password: String): RequestState<FirebaseUser> {
        return loginRepositoty.createUser(user(email, password))
    }

    private fun user(email: String, password: String): User {
        return User(email, password)
    }

    fun logout() {
        loginRepositoty.logout()
    }
}