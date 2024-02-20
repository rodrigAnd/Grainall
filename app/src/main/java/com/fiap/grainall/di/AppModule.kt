package com.fiap.grainall.di

import com.fiap.grainall.feature.alimento.data.AlimentoRepository
import com.fiap.grainall.feature.alimento.presenter.viewmodel.EstadoAppViewModel
import com.fiap.grainall.feature.alimento.presenter.viewmodel.FormularioAlimentoViewModel
import com.fiap.grainall.feature.alimento.presenter.viewmodel.ListaAlimentosViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module





val viewModelModule = module {
    viewModel<FormularioAlimentoViewModel> { FormularioAlimentoViewModel(get()) }
    viewModel<ListaAlimentosViewModel> { ListaAlimentosViewModel(get()) }
    viewModel<EstadoAppViewModel> { EstadoAppViewModel() }
}

val repositoryModule = module {
    single<AlimentoRepository> { AlimentoRepository(get(), get()) }
}

val firebaseModule = module {
    single<FirebaseFirestore> { Firebase.firestore }
    single<FirebaseStorage> {Firebase.storage}
}

val appModules: List<Module> = listOf(
    viewModelModule,
    repositoryModule,
    firebaseModule
)