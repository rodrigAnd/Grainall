package com.fiap.grainall.feature.alimento.di.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiap.grainall.R
import com.fiap.grainall.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException



class LoginFragment : Fragment() {

    private val REQUEST_CODE_GOOGLE_SIGN_IN = 1 /* unique request id */
    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
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
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireActivity())

       // if (account != null) {
          //  findNavController().navigate(R.id.action_loginFragment_to_myFoodFragment)
            findNavController().navigate(R.id.action_loginFragment_to_listarAlimento)
       // }

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
            //    if (account != null) {
                 //   findNavController().navigate(R.id.action_loginFragment_to_myFoodFragment)
                    findNavController().navigate(R.id.action_loginFragment_to_listarAlimento)
            //    }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}