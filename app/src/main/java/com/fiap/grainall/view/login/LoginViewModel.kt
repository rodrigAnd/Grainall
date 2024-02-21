package com.fiap.grainall.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.fiap.grainall.domain.usecase.LoginUseCase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val logionUseCase: LoginUseCase) : ViewModel() {

    private val _loginState = MutableStateFlow<RequestState<FirebaseUser>?>(null)
    val loginState : StateFlow<RequestState<FirebaseUser>?> = _loginState

    fun login(user: User) {
        viewModelScope.launch {
            if (user.email.isNullOrBlank().not() || user.password.isNullOrBlank().not()) {
                _loginState.value = RequestState.Loading
                val response = logionUseCase.login(user)
                _loginState.value = response
            } else {
                _loginState.value = RequestState.Error(Exception("Favor preencher todos os campos"))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            logionUseCase.logout()
        }
    }
}