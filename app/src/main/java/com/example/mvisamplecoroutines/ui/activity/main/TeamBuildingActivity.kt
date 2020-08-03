package com.example.mvisamplecoroutines.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.mvisamplecoroutines.R
import com.example.mvisamplecoroutines.databinding.ActivityMainTeambuidlingBinding
import com.example.mvisamplecoroutines.utils.gone
import com.example.mvisamplecoroutines.utils.onItemSelectedEvents
import com.example.mvisamplecoroutines.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.LazyThreadSafetyMode.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TeamBuildingActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainTeambuidlingBinding.inflate(layoutInflater) }
    private val navController: NavController by lazy(NONE) { initNavController() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupActionBar()
        setupBottomNavigation()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
                || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(navController)
                || super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val currentFragment = navController.currentDestination
        when (currentFragment?.id) {
            R.id.homeFragment, R.id.loginFragment -> finish()
            else -> navController.popBackStack()
        }
    }

    fun showBottomNavigation() {
        binding.bottomNavigation.show()
    }

    fun hideBottomNavigation() {
        binding.bottomNavigation.gone()
    }

    private fun initNavController(): NavController {
        return supportFragmentManager.findFragmentById(binding.navHostContainerTeamBuilding.id)
            ?.findNavController() ?: findNavController(binding.navHostContainerTeamBuilding.id)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.onItemSelectedEvents()
            .onEach { it.onNavDestinationSelected(navController) }
            .launchIn(lifecycleScope)
    }

    private fun setupActionBar() {
        binding.toolbar.setupWithNavController(navController)
    }

    companion object {
        private val TAG = this::class.java.simpleName
        fun getInstance(context: Context): Intent {
            return Intent(context, TeamBuildingActivity::class.java)
        }
    }
}
