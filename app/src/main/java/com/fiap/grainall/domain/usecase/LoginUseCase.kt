package com.fiap.grainall.domain.usecase

import androidx.lifecycle.LiveData
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.repository.LoginRepository
import com.fiap.grainall.domain.state.RequestState
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser


class LoginUseCase(private val loginRepositoty: LoginRepository) {
    suspend fun login(user: User): RequestState<FirebaseUser>  {
        return loginRepositoty.login(user)
    }
}