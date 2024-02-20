package com.fiap.grainall.domain.repository

import android.util.Log
import com.fiap.grainall.domain.model.Alimento
import com.fiap.grainall.utils.Constants.FIRESTORE_COLLECTION_GRAINALLS
import com.fiap.grainall.utils.Constants.TAG
import com.fiap.grainall.utils.Resultado
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class AlimentoRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    suspend fun salva(alimento: Alimento): String {
        val documento = firestore.collection(FIRESTORE_COLLECTION_GRAINALLS)
            .document()
        documento
            .set(DocumentoAlimento(alimento))
            .await()
        return documento.id
    }

    suspend fun enviaImagem(alimentoId: String, imagem: ByteArray) {
        GlobalScope.launch {
            try {
                val documento = firestore.collection(FIRESTORE_COLLECTION_GRAINALLS)
                    .document(alimentoId)

                documento
                    .update(mapOf("temImagem" to true))
                    .await()

                val referencia = storage.reference.child("alimentos/$alimentoId.jpg")
                referencia.putBytes(imagem).await()
                val url = referencia.downloadUrl.await()

                documento
                    .update(mapOf("imagem" to url.toString()))
                    .await()
            } catch (e: Exception) {
                Log.e(TAG, "enviaImagem: falha ao enviar a imagem", e)
            }
        }
    }

    suspend fun removeImagem(alimentoId: String) {
        GlobalScope.launch {
            try {
                val referencia = storage.reference.child("alimentos/$alimentoId.jpg")
                referencia.delete().await()
            } catch (e: Exception) {
                Log.e(TAG, "removeImagem: falha ao remover a imagem", e)
            }
        }
    }

    suspend fun edita(alimento: Alimento) {
        val alimentoId =
            alimento.id ?: throw IllegalArgumentException("Id não pode ser nulo ao editar um alimento")
        firestore.collection(FIRESTORE_COLLECTION_GRAINALLS)
            .document(alimentoId)
            .set(DocumentoAlimento(alimento))
            .await()
    }

    fun buscaTodos() = callbackFlow<Resultado<List<Alimento>>> {
        val listener = firestore.collection(FIRESTORE_COLLECTION_GRAINALLS)
            .addSnapshotListener { query, erro ->
                if (erro != null) {
                    //   offer(Resultado.Erro(erro))

                    trySend(Resultado.Erro(erro)).isSuccess
                    close(erro) // Fechar o canal em caso de erro
                    return@addSnapshotListener
                }

                val alimentos = query?.documents?.mapNotNull { documento ->
                    documento.paraPost()
                } ?: return@addSnapshotListener

                if (!isClosedForSend) { // Verificar se o canal ainda está aberto para enviar valores
                    //        offer(Resultado.Sucesso(alimentos))
                    trySend(Resultado.Sucesso(alimentos)).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    fun buscaPorId(id: String) = callbackFlow<Alimento?> {
        val listener = firestore.collection(FIRESTORE_COLLECTION_GRAINALLS)
            .document(id)
            .addSnapshotListener { documento, _ ->
                if (!isClosedForSend) { // Verificar se o canal ainda está aberto para enviar valores
                    //          offer(documento?.paraPost())
                    trySend(documento?.paraPost()).isSuccess
                }
            }

        awaitClose { listener.remove() }
    }

    suspend fun remove(id: String) {
        firestore.collection(FIRESTORE_COLLECTION_GRAINALLS)
            .document(id)
            .delete().await()
    }

    private fun DocumentSnapshot.paraPost(): Alimento? {
        return this.toObject(DocumentoAlimento::class.java)?.paraAlimento(this.id)
    }

}

private class DocumentoAlimento(
    val mensagem: String = "",
    val imagem: String? = null,
    val temImagem: Boolean = false
) {

    constructor(alimento: Alimento) : this(
        mensagem = alimento.mensagem,
        imagem = alimento.imagem,
        temImagem = alimento.temImagem
    )

    fun paraAlimento(id: String? = null) = Alimento(
        id = id,
        mensagem = mensagem,
        imagem = imagem,
        temImagem = temImagem
    )

}

