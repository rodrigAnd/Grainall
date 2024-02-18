package com.fiap.grainall.di


import com.fiap.grainall.domain.repository.LoginRepository
import com.fiap.grainall.domain.repository.LoginRepositoryImpl
import com.fiap.grainall.domain.usecase.LoginUseCase
import com.fiap.grainall.view.login.LoginViewModel
import com.google.firebase.database.FirebaseDatabase
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel {
        LoginViewModel(get())
    }
    single <LoginRepository> {
        LoginRepositoryImpl(FirebaseDatabase.getInstance())
    }
    single { LoginUseCase(get()) }
}