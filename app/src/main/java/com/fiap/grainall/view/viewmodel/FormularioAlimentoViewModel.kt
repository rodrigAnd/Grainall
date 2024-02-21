package com.fiap.grainall.view.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.fiap.grainall.domain.model.Alimento
import com.fiap.grainall.domain.repository.AlimentoRepository
import com.fiap.grainall.domain.state.RequestState
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class FormularioAlimentoViewModel(
    private val repository: AlimentoRepository
) : ViewModel() {

    private val _imagemCarregada = MutableLiveData<String?>()
    val imagemCarregada: LiveData<String?> = _imagemCarregada

    fun buscaAlimento(id: String) = repository.buscaPorId(id).asLiveData()

    fun salva(alimento: Alimento, imagem: ByteArray) =
        liveData<RequestState<Unit>> {
            try {
                val id = repository.salva(alimento)
                emit(RequestState.Success())
                coroutineScope {
                    launch {
                        tentaEnviarImagem(id, imagem)
                    }
                }
            } catch (e: Exception) {
                Log.e("FormAlimentoVM", "salva: falha ao enviar alimento", e)
                emit(RequestState.Error(e))
            }
        }

    fun remove(alimentoId: String) =
        liveData<RequestState<Unit>> {
            try {
                repository.remove(alimentoId)
                emit(RequestState.Success())
                repository.removeImagem(alimentoId)
            } catch (e: Exception) {
                emit(RequestState.Error(e))
            }
        }

    fun edita(alimento: Alimento, imagem: ByteArray) =
        liveData<RequestState<Unit>> {
            try {
                repository.edita(alimento)
                emit(RequestState.Success())
                alimento.id?.let { alimentoId ->
                    coroutineScope {
                        launch {
                            tentaEnviarImagem(alimentoId, imagem)
                        }
                    }
                }
            } catch (e: Exception) {
                emit(RequestState.Error(e))
            }
        }

    private suspend fun tentaEnviarImagem(alimentoId: String, imagem: ByteArray) {
        imagemCarregada.value?.let {
            repository.enviaImagem(alimentoId, imagem)
        }
    }

    fun atualizaImagem(imagem: String) {

        _imagemCarregada.postValue(imagem)
    }

    fun removeImagem() {
        _imagemCarregada.postValue(null)
    }
}