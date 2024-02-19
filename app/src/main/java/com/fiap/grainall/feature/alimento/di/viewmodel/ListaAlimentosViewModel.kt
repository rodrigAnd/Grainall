package com.fiap.grainall.feature.alimento.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fiap.grainall.feature.alimento.data.AlimentoRepository

class ListaAlimentosViewModel(private val repository: AlimentoRepository) : ViewModel() {

    fun buscaTodos() = repository.buscaTodos().asLiveData()

}