package com.fiap.grainall.feature.alimento.di.viewmodel





import android.util.Log
import androidx.lifecycle.*
import com.fiap.grainall.feature.alimento.data.AlimentoRepository
import com.fiap.grainall.model.Alimento
import com.fiap.grainall.utils.Resultado
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


class FormularioAlimentoViewModel(
    private val repository: AlimentoRepository
) : ViewModel() {

    private val _imagemCarregada = MutableLiveData<String?>()
    val imagemCarregada: LiveData<String?> = _imagemCarregada

    fun buscaAlimento(id: String) = repository.buscaPorId(id).asLiveData()

    fun salva(alimento: Alimento, imagem: ByteArray) =
        liveData<Resultado<Unit>> {
            try {
                val id = repository.salva(alimento)
                emit(Resultado.Sucesso())
                coroutineScope {
                    launch {
                        tentaEnviarImagem(id, imagem)
                    }
                }
            } catch (e: Exception) {
                Log.e("FormAlimentoVM", "salva: falha ao enviar alimento", e)
                emit(Resultado.Erro(e))
            }
        }

    fun remove(alimentoId: String) =
        liveData<Resultado<Unit>> {
            try {
                repository.remove(alimentoId)
                emit(Resultado.Sucesso())
                repository.removeImagem(alimentoId)
            } catch (e: Exception) {
                emit(Resultado.Erro(e))
            }
        }

    fun edita(alimento: Alimento, imagem: ByteArray) =
        liveData<Resultado<Unit>> {
            try {
                repository.edita(alimento)
                emit(Resultado.Sucesso())
                alimento.id?.let { alimentoId ->
                    coroutineScope {
                        launch {
                            tentaEnviarImagem(alimentoId, imagem)
                        }
                    }
                }
            } catch (e: Exception) {
                emit(Resultado.Erro(e))
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