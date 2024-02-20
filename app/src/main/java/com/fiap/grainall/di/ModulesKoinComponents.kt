package com.fiap.grainall.di


import com.fiap.grainall.domain.repository.AlimentoRepository
import com.fiap.grainall.domain.repository.LoginRepository
import com.fiap.grainall.domain.repository.LoginRepositoryImpl
import com.fiap.grainall.domain.usecase.LoginUseCase
import com.fiap.grainall.view.viewmodel.EstadoAppViewModel
import com.fiap.grainall.view.viewmodel.FormularioAlimentoViewModel
import com.fiap.grainall.view.viewmodel.ListaAlimentosViewModel
import com.fiap.grainall.view.login.LoginViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule = module {
    viewModel{ FormularioAlimentoViewModel(get()) }
    viewModel { ListaAlimentosViewModel(get()) }
    viewModel { EstadoAppViewModel() }
    viewModel { LoginViewModel(get()) }
}

val repositoryModule = module {
    single { AlimentoRepository(get(), get()) }
    single <LoginRepository> { LoginRepositoryImpl(FirebaseDatabase.getInstance()) }
}

val firebaseModule = module {
    single { Firebase.firestore }
    single { Firebase.storage}
}

val useCaseModule = module {
    single { LoginUseCase(get()) }
}

val appModules: List<Module> = listOf(
    viewModelModule,
    repositoryModule,
    firebaseModule,
    useCaseModule
)