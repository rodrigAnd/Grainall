package com.fiap.grainall

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.fiap.grainall.databinding.FragmentMyFoodBinding
import com.fiap.grainall.domain.extensions.fullScreen
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class MyFoodFragment : Fragment() {

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    private val binding: FragmentMyFoodBinding by lazy {
        FragmentMyFoodBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_my_food, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fullScreen()
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    // Handle the back button event
                    activity?.finish()
                }
            })

    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater
    ) {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.my_food_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_my_food_menu -> {
                // Handle the camera action
                Toast.makeText(requireContext(), "cliclou", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}