package com.fiap.grainall

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment

import com.fiap.grainall.databinding.ActivityMainBinding
import com.fiap.grainall.feature.alimento.presenter.viewmodel.EstadoAppViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private val controlador by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }
    }
    private val viewModel: EstadoAppViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.activityMainToolbar)
        controlador.addOnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
            title = navDestination.label
        }
        viewModel.componentes.observe(this) {
            it?.let { components ->
                binding.activityMainToolbar.visibility =
                    if (components.appBar) VISIBLE
                    else GONE
            }
        }
    }

}