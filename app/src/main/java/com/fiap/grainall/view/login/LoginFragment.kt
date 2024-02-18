package com.fiap.grainall.view.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.fiap.grainall.R
import com.fiap.grainall.databinding.FragmentLoginBinding
import com.fiap.grainall.domain.model.User
import com.fiap.grainall.domain.state.RequestState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
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
        //selectedFirebase()
        loginGoogle()
        initButtons()
        initObserver()
    }

    private fun initObserver() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is RequestState.Error -> {
                    Toast.makeText(requireContext(), "Erro ", Toast.LENGTH_SHORT).show()
                }

                is RequestState.Success -> {
                    findNavController().navigate(R.id.action_loginFragment_to_myFoodFragment)
                }

                else -> {
                    //Nothing to do
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

            viewModel.login(User(email, password))
        }
    }

//    private fun selectedFirebase() {
////        // Write a message to the database
////        val database = Firebase.database
////        val myRef = database.getReference("message")
////
////        val user = User("Rodrigo", "rodrigo.r.guilherme@outlook.com", "123456")
//
//        myRef.setValue(user)
//
//        // Read from the database
//        myRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                val value = dataSnapshot.getValue<User>()
//                Log.d("read", "Value is: $value")
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Failed to read value
//                Log.w("falha read", "Failed to read value.", error.toException())
//            }
//        })
//    }

    private fun loginGoogle() {
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(requireActivity(), gso)
        val account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(requireActivity())

        if (account != null) {
            findNavController().navigate(R.id.action_loginFragment_to_myFoodFragment)
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
                    findNavController().navigate(R.id.action_loginFragment_to_myFoodFragment)
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }
}
