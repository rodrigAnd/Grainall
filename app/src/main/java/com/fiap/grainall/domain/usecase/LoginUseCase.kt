package com.fiap.grainall.domain.usecase

import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.repository.LoginRepository


class LoginUseCase(private val loginRepositoty: LoginRepository) {
    suspend fun login(user: User): Boolean {
        return if (user.email.isNullOrBlank().not() || user.password.isNullOrBlank()) {
            loginRepositoty.login(user)
        } else {
            false
        }
    }
}