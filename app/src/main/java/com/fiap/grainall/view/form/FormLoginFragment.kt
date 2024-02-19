package com.fiap.grainall.view.form

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiap.grainall.R
import com.fiap.grainall.databinding.FragmentFormLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

/**
 * A simple [Fragment] subclass.
 * Use the [FormLoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FormLoginFragment : Fragment() {

    private val binding: FragmentFormLoginBinding by lazy {
        FragmentFormLoginBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initButtons()
    }

    private fun initButtons() {
        binding.buttonCreatedAccount.setOnClickListener {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            val user = auth.currentUser
                            Log.d("Sucesso", "sucesso: $user")
                            findNavController().navigate(R.id.action_formLoginFragment_to_loginFragment)

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                requireContext(),
                                "Erro ao criar usuário",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            } else
                Toast.makeText(requireContext(), "Preencha todos os campos", Toast.LENGTH_SHORT).show()
        }
    }
}