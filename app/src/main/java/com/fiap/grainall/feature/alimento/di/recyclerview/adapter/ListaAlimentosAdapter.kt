package com.fiap.grainall.feature.alimento.di.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import coil.load
import com.fiap.grainall.R
import com.fiap.grainall.databinding.ItemAlimentoBinding
import com.fiap.grainall.model.Alimento


class ListaAlimentosAdapter(
    private val context: Context,
    alimentos: List<Alimento> = listOf(),
    val quandoClicaNoItem: (id: String) -> Unit = {}
) : RecyclerView.Adapter<ListaAlimentosAdapter.ViewHolder>() {

    private val alimentos: MutableList<Alimento> = alimentos.toMutableList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ViewHolder(
        ItemAlimentoBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val alimento = alimentos[position]
        holder.vincula(alimento)
    }

    override fun getItemCount(): Int = alimentos.size

    fun atualiza(alimentos: List<Alimento>) {
        this.alimentos.clear()
        this.alimentos.addAll(alimentos)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemAlimentoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var alimento: Alimento

        init {
            binding.root.setOnClickListener {
                if (::alimento.isInitialized) {
                    alimento.id?.let {
                        quandoClicaNoItem(it)
                    }
                }
            }
        }

        fun vincula(alimento: Alimento) {
            this.alimento = alimento

            binding.itemAlimentoMensagem.text = alimento.mensagem

            configuraImagemDoAlimento(alimento)
        }

        private fun configuraImagemDoAlimento(alimento: Alimento) {
            val visibilidade = if (alimento.temImagem) {
                binding.itemAlimentoImagem.load(R.drawable.imagem_carregando_placeholder)
                VISIBLE
            } else {
                GONE
            }
            binding.itemAlimentoImagem.visibility = visibilidade
            alimento.imagem?.let { imagem ->
                binding.itemAlimentoImagem.load(imagem) {
                    placeholder(R.drawable.imagem_carregando_placeholder)
                    crossfade(true)
                }
            }
        }

    }

}
