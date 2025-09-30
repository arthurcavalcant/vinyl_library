package br.ifsp.dm2.vinyllibrary

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import br.ifsp.dm2.vinyllibrary.databinding.ActivityMainBinding
import br.ifsp.dm2.vinyllibrary.viewmodel.VinylViewModel
import br.ifsp.dm2.vinyllibrary.viewmodel.VinylViewModelFactory
import br.ifsp.dm2.vinyllibrary.R

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val vinylViewModel: VinylViewModel by viewModels {
        VinylViewModelFactory((application as VinylLibraryApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the toolbar as the ActionBar
        setSupportActionBar(binding.toolbar)

        setupNavigation()
    }

    private fun setupNavigation() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
