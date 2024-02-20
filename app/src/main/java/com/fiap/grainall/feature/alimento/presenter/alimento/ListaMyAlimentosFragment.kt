package com.fiap.grainall.feature.alimento.presenter.alimento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiap.grainall.databinding.ListaAlimentosBinding
import com.fiap.grainall.extensions.snackbar
import com.fiap.grainall.feature.alimento.presenter.recyclerview.adapter.ListaAlimentosAdapter
import com.fiap.grainall.feature.alimento.presenter.viewmodel.EstadoAppViewModel
import com.fiap.grainall.feature.alimento.presenter.viewmodel.ListaAlimentosViewModel
import com.fiap.grainall.utils.Resultado
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaAlimentosFragment : Fragment() {

    private var _binding: ListaAlimentosBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy {
        ListaAlimentosAdapter(requireContext()) { alimentoId ->
            vaiParaFormularioAlimento(alimentoId)
        }
    }
    private val controlador by lazy {
        findNavController()
    }
    private val viewModel: ListaAlimentosViewModel by viewModel()
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ListaAlimentosBinding.inflate(
            inflater,
            container,
            false
        ).let {
            _binding = it
            it.root
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        estadoAppViewModel.limpaComponentes()
        configuraRecyclerView()
        configuraFab()
    }

    private fun configuraFab() {
        binding.listaAlimentosFabAdiciona.setOnClickListener {
            vaiParaFormularioAlimento()
        }
    }

    private fun configuraRecyclerView() {
        binding.listaAlimentosRecyclerview.adapter = adapter
        viewModel.buscaTodos().observe(viewLifecycleOwner) {
            it?.let { resultado ->
                when (resultado) {
                    is Resultado.Sucesso -> resultado.dado?.let(adapter::atualiza)
                    is Resultado.Erro -> view?.snackbar("Falha ao encontrar novos alimentos")
                    else -> {}
                }
            }
            configuraVisibilidadeDosContainers()
        }
    }

    private fun configuraVisibilidadeDosContainers() {
        if (adapter.itemCount > 0) {
            binding.listaAlimentosContainerAvaliacoesNaoEncontradas.visibility = GONE
            binding.listaAlimentosRecyclerview.visibility = VISIBLE
            return
        }
        binding.listaAlimentosContainerAvaliacoesNaoEncontradas.visibility = VISIBLE
        binding.listaAlimentosRecyclerview.visibility = GONE
    }

    private fun vaiParaFormularioAlimento(alimentoId: String? = null) {
        ListaAlimentosFragmentDirections
            .acaoListaAlimentosParaFormularioAlimento(alimentoId)
            .let(controlador::navigate)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}