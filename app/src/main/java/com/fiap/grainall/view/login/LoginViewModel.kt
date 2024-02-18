package com.fiap.grainall.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.fiap.grainall.domain.usecase.LoginUseCase
import kotlinx.coroutines.launch

class LoginViewModel(private val logionUseCase: LoginUseCase) : ViewModel() {

    val loginState = MutableLiveData<RequestState<String>>()
    val loggedUserState = MutableLiveData<RequestState<String>>()

    fun login(user: User) {
        viewModelScope.launch {
            if (user.email.isNullOrBlank().not() || user.password.isNullOrBlank().not()) {
                val response = logionUseCase.login(user)
                if (response) {
                    loginState.value = RequestState.Success("sucesso")
                } else {
                    loginState.value = RequestState.Error(Exception("Login ou senha invalido"))
                }
            } else {
                loginState.value = RequestState.Error(Exception("Favor preencher todos os campos"))
            }
        }
    }
}