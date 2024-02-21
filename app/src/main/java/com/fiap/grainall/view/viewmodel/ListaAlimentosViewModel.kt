package com.fiap.grainall.view.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.fiap.grainall.domain.repository.AlimentoRepository

class ListaAlimentosViewModel(private val repository: AlimentoRepository) : ViewModel() {

    fun buscaTodos() = repository.buscaTodos().asLiveData()

}