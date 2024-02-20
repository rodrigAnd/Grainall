package com.fiap.grainall.feature.alimento.presenter.alimento


import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.fiap.grainall.R
import com.fiap.grainall.databinding.FormularioAlimentoBinding
import com.fiap.grainall.databinding.OpcoesImagemAlimentoBinding
import com.fiap.grainall.extensions.snackbar
import com.fiap.grainall.feature.alimento.presenter.viewmodel.Componentes
import com.fiap.grainall.feature.alimento.presenter.viewmodel.EstadoAppViewModel
import com.fiap.grainall.feature.alimento.presenter.viewmodel.FormularioAlimentoViewModel
import com.fiap.grainall.model.Alimento
import com.fiap.grainall.utils.Resultado
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import org.koin.androidx.viewmodel.ext.android.viewModel


private const val REQUEST_IMAGE_GET = 1
private val REQUEST_IMAGE_CAPTURE = 42
private val CAMERA_PERMISSION_REQUEST_CODE = 101
private val FILE_NAME = "photo.jpg"
private lateinit var photoFile: File

class FormularioAlimentoFragment : Fragment() {

    private var _binding: FormularioAlimentoBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FormularioAlimentoViewModel by viewModel()
    private val controlador by lazy {
        findNavController()
    }
    private val estadoAppViewModel: EstadoAppViewModel by sharedViewModel()
    private val argumentos by navArgs<FormularioAlimentoFragmentArgs>()
    private val alimentoId: String? by lazy { argumentos.alimentoId }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FormularioAlimentoBinding.inflate(
        inflater,
        container,
        false
    ).let { binding ->
        this._binding = binding
        binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        estadoAppViewModel.setComponentes(
            Componentes(
                appBar = true
            )
        )
        tentaCarregarAlimento()
        configuraCarregamentoDeImagem()
        configuraAlimentoImagem()
    }

    private fun configuraAlimentoImagem() {


        binding.formularioAlimentoImagem.setOnClickListener {
            apresentaDialogo()
        }
    }

    private fun apresentaDialogo() {
        val dialogo = BottomSheetDialog(requireContext())
        val bindingOpcoesImagem = OpcoesImagemAlimentoBinding.inflate(layoutInflater)
        bindingOpcoesImagem.opcoesImagemAlimentoGaleria.setOnClickListener {
       //     fotografaImagem()
             apresentaGaleria()
            dialogo.behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        bindingOpcoesImagem.opcoesImagemAlimentoRemover.setOnClickListener {
            viewModel.removeImagem()
            dialogo.behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        bindingOpcoesImagem.opcoesImagemAlimentoFotografar.setOnClickListener {
            fotografaImagem()
            dialogo.behavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
        dialogo.setContentView(bindingOpcoesImagem.root)
        dialogo.show()
    }

    private fun apresentaGaleria() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        startActivityForResult(intent, REQUEST_IMAGE_GET)
    }

    private fun fotografaImagem() {

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )

        } else {
            dispatchTakePictureIntent()
        }

    }


    private fun configuraCarregamentoDeImagem() {
        viewModel.imagemCarregada.observe(viewLifecycleOwner) {
            it?.let { imagem ->

                binding.formularioAlimentoImagem.load(imagem)
                return@observe
            }
            binding.formularioAlimentoImagem.load(R.drawable.imagem_insercao_padrao)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_formulario_alimento, menu)
        if (alimentoId == null) {
            menu.findItem(R.id.menu_formulario_alimento_remove).isVisible = false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_formulario_alimento_enviar -> criaAlimento()
            R.id.menu_formulario_alimento_remove -> apresentaDialogoDeRemocao()
            R.id.menu_formulario_alimento_fotografar -> criaAlimento()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun criaAlimento() {

        val mensagem = binding.formularioAlimentoDescricao.text.toString()

        val alimentoNovo = Alimento(
            id = alimentoId,
            mensagem = mensagem
        )
        enviaAlimento(alimentoNovo)
    }

    private fun enviaAlimento(alimento: Alimento) {
        if (alimento.id != null) {
            edita(alimento)
        } else {
            salva(alimento)
        }
    }

    private fun edita(alimento: Alimento) {
        val imagem = devolveImagemDoAlimento()
        viewModel.edita(alimento, imagem).observe(viewLifecycleOwner) {
            it?.let { resultado ->
                when (resultado) {
                    is Resultado.Sucesso -> controlador.popBackStack()
                    is Resultado.Erro -> binding.formularioAlimentoCoordinator
                        .snackbar(mensagem = "Alimento não foi editado")
                }
            }
        }
    }

    private fun salva(alimento: Alimento) {
        val imagem = devolveImagemDoAlimento()
        viewModel.salva(alimento, imagem).observe(viewLifecycleOwner) { resultado ->
            when (resultado) {
                is Resultado.Sucesso -> controlador.popBackStack()
                is Resultado.Erro -> binding.formularioAlimentoCoordinator
                    .snackbar(mensagem = "Alimento não foi enviado")
            }
        }
    }

    private fun devolveImagemDoAlimento(): ByteArray {
        val imageView = binding.formularioAlimentoImagem
        val bitmap = imageView.drawable.toBitmap()
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        return baos.toByteArray()
    }

    private fun tentaCarregarAlimento() {
        alimentoId?.let { id ->
            requireActivity().title = "Editar alimento"
            viewModel.buscaAlimento(id).observe(viewLifecycleOwner) { alimento ->
                alimento?.let(::preencheCampos)
            }
        }
    }

    private fun preencheCampos(alimento: Alimento) {

        binding.formularioAlimentoDescricao.setText(alimento.mensagem)

        alimento.imagem?.let { imagem ->
            viewModel.atualizaImagem(imagem)
        }
    }

    private fun apresentaDialogoDeRemocao() {
        AlertDialog.Builder(requireContext())
            .setTitle("Removendo Alimento")
            .setMessage("Você quer remover esse alimento?")
            .setPositiveButton("Sim") { _, _ ->
                alimentoId?.let(this::remove)
            }
            .setNegativeButton("Não")
            { _, _ -> }
            .show()
    }

    private fun remove(alimentoId: String) {
        viewModel.remove(alimentoId).observe(viewLifecycleOwner) {
            view?.snackbar("Alimento foi removido!")
            controlador.popBackStack()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                binding.formularioAlimentoCoordinator
                .snackbar(mensagem = "Permissão negada - Alimento não foi fotografado")

            }
        }
    }

    private fun dispatchTakePictureIntent() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = getPhotoFile(FILE_NAME)

        val fileProvider = FileProvider.getUriForFile(
            requireContext(),
            "com.fiap.grainall.fileprovider",
            photoFile
        )
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
    }

    private fun getPhotoFile(fileName: String): File {
        val storageDirectory =
            requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

//override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//    if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
//       data?.data?.let { imagem ->
//           viewModel.atualizaImagem(imagem.toString())
//       }
//   }
//}


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            binding?.formularioAlimentoImagem?.setImageBitmap(takenImage)
            data?.data?.let { imagem ->
                viewModel.atualizaImagem(imagem.toString())
            }
        } else if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            data?.data?.let { imagem ->
                viewModel.atualizaImagem(imagem.toString())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}