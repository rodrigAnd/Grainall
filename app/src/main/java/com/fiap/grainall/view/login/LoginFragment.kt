package com.fiap.grainall.view.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.fiap.grainall.R
import com.fiap.grainall.databinding.FragmentLoginBinding
import com.fiap.grainall.domain.extensions.fullScreen
import com.fiap.grainall.domain.extensions.hideKeyboard
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val REQUEST_CODE_GOOGLE_SIGN_IN = 1 /* unique request id */
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    private val viewModel: LoginViewModel by viewModel()
    private val binding: FragmentLoginBinding by lazy {
        FragmentLoginBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        fullScreen()
        loginGoogle()
        initButtons()
        initObserver()
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loginState.collect {
                when (it) {
                    is RequestState.Success -> {
                        binding.loadingAnimationLogin.visibility = View.INVISIBLE
                        findNavController().navigate(R.id.action_loginFragment_to_listaAlimentosFragment)
                    }

                    is RequestState.Error -> {
                        val animShake = AnimationUtils.loadAnimation(requireContext(), R.anim.shake)
                        binding.containerLogin.startAnimation(animShake)
                        binding.tvPasswordFeedback.text = it.throwable.message
                        binding.loadingAnimationLogin.visibility = View.INVISIBLE
                    }

                    is RequestState.Loading -> {
                        binding.loadingAnimationLogin.visibility = View.VISIBLE
                    }

                    else -> {
                        // do nothing
                    }
                }
            }
        }
    }

        private fun initButtons() {
            binding.singUpButton.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_formLoginFragment)
            }

            binding.buttonLogin.setOnClickListener {
                val email = binding.editTextEmailAddressLogin.text.toString()
                val password = binding.editTextPasswordLogin.text.toString()

                lifecycleScope.launch {
                    viewModel.login(User(email, password))
                }
            }
        }

        private fun loginGoogle() {
            gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
            gsc = GoogleSignIn.getClient(requireActivity(), gso)
            val account: GoogleSignInAccount? =
                GoogleSignIn.getLastSignedInAccount(requireActivity())

            if (account != null) {
                findNavController().navigate(R.id.action_loginFragment_to_listaAlimentosFragment)
            }

            binding.signInButtonGoogle.setOnClickListener {
                val signInIntent = gsc.signInIntent
                startActivityForResult(signInIntent, REQUEST_CODE_GOOGLE_SIGN_IN)
            }

            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                requireActivity().finish()
            }
        }

        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == REQUEST_CODE_GOOGLE_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    val account = task.getResult(ApiException::class.java)
                    if (account != null) {
                        findNavController().navigate(R.id.action_formularioAlimento_to_listaAlimentos)
                    }
                } catch (e: Exception) {
                    e.stackTrace
                }
            }
        }
    }
