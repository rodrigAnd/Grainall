package com.fiap.grainall.view.alimento

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiap.grainall.R
import com.fiap.grainall.databinding.FragmentAlimentosAdapterBinding
import com.fiap.grainall.domain.extensions.onBackPress
import com.fiap.grainall.domain.extensions.snackbar
import com.fiap.grainall.domain.state.RequestState
import com.fiap.grainall.view.adapter.ListaAlimentosAdapter
import com.fiap.grainall.view.login.LoginViewModel
import com.fiap.grainall.view.viewmodel.Componentes
import com.fiap.grainall.view.viewmodel.EstadoAppViewModel
import com.fiap.grainall.view.viewmodel.ListaAlimentosViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ListaAlimentosFragment : Fragment() {

    private var _binding: FragmentAlimentosAdapterBinding? = null
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
        return FragmentAlimentosAdapterBinding.inflate(
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
        configuraRecyclerView()
        configuraFab()
        onBackPress()
        estadoAppViewModel.limpaComponentes()
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
                    is RequestState.Success -> resultado.data?.let(adapter::atualiza)
                    is RequestState.Error -> view?.snackbar("Falha ao encontrar novos alimentos")
                    else -> {}
                }
            }
        }
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